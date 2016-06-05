package com.hugo.xdaily.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hugo.xdaily.Network;
import com.hugo.xdaily.R;
import com.hugo.xdaily.adapter.NewsAdapter;
import com.hugo.xdaily.entry.BeforeStroy;
import com.hugo.xdaily.entry.LatestStory;
import com.hugo.xdaily.entry.Stroy;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.news_list)
    RecyclerView storyList;
    @Bind(R.id.swip_refresh)
    SwipeRefreshLayout swipeRefresh;

    private Context mContext = this;
    private LinearLayoutManager layoutManager;
    private NewsAdapter adapter;

    private List<Stroy> stories;
    private List<Stroy> newStories;
    private Date currentDate = new Date();
    private String date;
    private int flag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        initNews(false);
    }

    private void initNews(final boolean isRefresh) {
        stories = new ArrayList<>();
        Network.getZhihuApi().getLatest()
                .subscribeOn(Schedulers.io())
                .flatMap(new Func1<LatestStory, Observable<Stroy>>() {
                    @Override
                    public Observable<Stroy> call(LatestStory latestStory) {
                        date = latestStory.getDate();
                        return Observable.from(latestStory.getStories());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Stroy>() {
                    @Override
                    public void onNext(Stroy stroy) {
                        stroy.setDate(date);
                        stories.add(stroy);
                    }

                    @Override
                    public void onCompleted() {
                        if (isRefresh) {
                            adapter.addStories(stories, true);
                            swipeRefresh.setRefreshing(false);
                            stories = null;
                        } else
                            initUI();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(mContext, "出错了", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void initUI() {
        adapter = new NewsAdapter(stories);
        layoutManager = new LinearLayoutManager(mContext);

        storyList.setLayoutManager(layoutManager);
        storyList.setAdapter(adapter);

        initListener();
    }

    private void initListener() {
        adapter.setOnItemCLickListenenr(new NewsAdapter.onItemClickListenenr() {
            @Override
            public void onClick(Stroy stroy) {
                int id = stroy.getId();
                Intent intent = new Intent(mContext, ContentActivity.class);
                intent.putExtra("id", id + "");
                startActivity(intent);
            }
        });

        storyList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
                    //如果是最后一个item，则加载下一页
                    if (lastVisibleItemPosition == layoutManager.getItemCount() - 1) {
                        loadMoreDate();
                    }
                }
            }
        });

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initNews(true);
            }
        });
    }

    /**
     * 加载更多
     */
    private void loadMoreDate() {
        newStories = new ArrayList<>();
        String previousDate = getPreviousDate();
        if (previousDate.equals("20130520")) {
            Toast.makeText(mContext, "没有更多了", Toast.LENGTH_SHORT).show();
            return;
        }
        Network.getZhihuApi().getBefore(previousDate)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<BeforeStroy, Observable<Stroy>>() {
                    @Override
                    public Observable<Stroy> call(BeforeStroy beforeStroy) {
                        date = beforeStroy.getDate();
                        return Observable.from(beforeStroy.getStories());
                    }
                })
                .subscribe(new Observer<Stroy>() {
                    @Override
                    public void onCompleted() {
                        adapter.addStories(newStories, false);
                        newStories = null;
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Stroy stroy) {
                        stroy.setDate(date);
                        newStories.add(stroy);
                    }
                });
    }


    private String getPreviousDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(currentDate);
        calendar.add(Calendar.DATE, -flag);
        flag++;
        return sdf.format(calendar.getTime());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_clear_cache) {
            //清除内存缓存
            Glide.get(mContext).clearMemory();
            //清除磁盘缓存
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Glide.get(mContext).clearDiskCache();
                    System.out.println("清理缓存");
                }
            }).start();
        }
        return super.onOptionsItemSelected(item);
    }
}
