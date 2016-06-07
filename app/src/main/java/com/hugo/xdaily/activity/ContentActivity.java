package com.hugo.xdaily.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hugo.xdaily.Network;
import com.hugo.xdaily.R;
import com.hugo.xdaily.entry.Content;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class ContentActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.fab)
    FloatingActionButton fab;
    @Bind(R.id.webview)
    WebView webView;
    @Bind(R.id.iv_title_img)
    ImageView titleImg;

    private Context mContent = this;
    private String id;
    private String share_url;
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        ButterKnife.bind(this);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        webView.getSettings().setJavaScriptEnabled(true);
        id = getIntent().getStringExtra("id");
        initData();
    }

    private void initData() {
        Network.getZhihuApi().getContent(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<Content, String>() {
                    @Override
                    public String call(Content content) {
                        share_url = content.getShare_url();
                        title = content.getTitle();
                        setTitleImg(content.getImage());
                        return structHtml(content.getBody(), content.getCss(), content.getJs());
                    }
                })
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        webView.loadDataWithBaseURL(null, s, "text/html", "utf-8", null);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Toast.makeText(mContent, "出错了", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void setTitleImg(String imageUrl) {
        Glide.with(mContent).load(imageUrl)
                .skipMemoryCache(true)
                .centerCrop()
                .into(titleImg);

    }

    public String structHtml(String htmlBody, List<String> cssList, List<String> jsList) {
        String deleteDiv = "<div class=\"img-place-holder\"></div>";
        htmlBody = htmlBody.replace(deleteDiv, "");
        StringBuilder htmlString = new StringBuilder("<html><head>");
        for (String css : cssList) {
            htmlString.append(setCssLink(css));
        }
        for (String js : jsList) {
            htmlString.append(setJsLink(js));
        }
        htmlString.append("</head><body>");
        htmlString.append(htmlBody);
        htmlString.append("</body></html>");
        return htmlString.toString();
    }

    public String setCssLink(String css) {
        return "<link rel=\"stylesheet\" href=\"" + css + "\">";
    }

    private String setJsLink(String js) {
        return "<script src=\"" + js + "\"></script>";
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @OnClick(R.id.fab)
    public void onClick() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, title + "\n" + share_url);
        intent.setType("text/plain");
        startActivity(Intent.createChooser(intent, "通过以下应用分享"));
    }
}
