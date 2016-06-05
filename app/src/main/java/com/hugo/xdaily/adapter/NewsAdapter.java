package com.hugo.xdaily.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hugo.xdaily.R;
import com.hugo.xdaily.entry.Stroy;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import rx.Observable;
import rx.functions.Action1;

/**
 * @auther Hugo
 * Created on 2016/6/2 11:05.
 */
public class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static int DATE = 0;
    private static int NORMAL = 1;
    private static List<Stroy> stories;

    public NewsAdapter(List<Stroy> stories) {
        this.stories = stories;
    }

    /**
     * 如果是第一页则更新，否则追加
     *
     * @param newStories
     * @param isIndex    是否第一页
     */
    public void addStories(List<Stroy> newStories, boolean isIndex) {
        if (isIndex) {
            stories = newStories;
        } else {
            Observable.from(newStories).subscribe(new Action1<Stroy>() {
                @Override
                public void call(Stroy stroy) {
                    stories.add(stroy);
                }
            });
        }
        notifyDataSetChanged();
    }

    private static onItemClickListenenr listenenr;

    public interface onItemClickListenenr {
        void onClick(Stroy stroy);
    }

    public void setOnItemCLickListenenr(onItemClickListenenr listenenr) {
        this.listenenr = listenenr;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return DATE;
        else if (stories.get(position).getDate() != stories.get(position - 1).getDate())
            return DATE;
        else
            return NORMAL;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == NORMAL)
            return new NormalHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_story_normal, parent, false));
        else
            return new DateHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_story_date, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof NormalHolder)
            ((NormalHolder) holder).initDate();
        else
            ((DateHolder) holder).initDate();
    }

    @Override
    public int getItemCount() {
        if (stories == null)
            return 0;
        return stories.size();
    }

    static class NormalHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView storyTitle;
        ImageView storyImg;
        LinearLayout rootView;
        private Stroy stroy;

        public NormalHolder(View itemView) {
            super(itemView);
            storyTitle = ButterKnife.findById(itemView, R.id.tv_story_title);
            storyImg = ButterKnife.findById(itemView, R.id.iv_story_img);
            rootView = ButterKnife.findById(itemView, R.id.root_view);
            rootView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (listenenr != null) {
                listenenr.onClick(stories.get(getAdapterPosition()));
            }
        }

        public void initDate() {
            stroy = stories.get(getAdapterPosition());
            storyTitle.setText(stroy.getTitle());
            Glide.with(itemView.getContext())
                    .load(stroy.getImages().get(0))
                    .into(storyImg);
        }
    }

    static class DateHolder extends NormalHolder {
        TextView tvDate;

        public DateHolder(View itemView) {
            super(itemView);
            tvDate = ButterKnife.findById(itemView, R.id.story_date);
        }

        @Override
        public void initDate() {
            super.initDate();
            tvDate.setText(setDate(super.stroy.getDate()));
        }

        public String setDate(String date) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            String today = sdf.format(new Date());
            if (today.equals(date))
                return "今日热闻";
            return date.substring(4, 6) + "月" + date.substring(6) + "日";
        }
    }
}
