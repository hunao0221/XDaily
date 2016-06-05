package com.hugo.xdaily.api;

import com.hugo.xdaily.entry.BeforeStroy;
import com.hugo.xdaily.entry.Content;
import com.hugo.xdaily.entry.LatestStory;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * @auther Hugo
 * Created on 2016/6/2 9:57.
 */
public interface ZhihuApi {
    //http://news-at.zhihu.com/api/4/news/latest
    @GET("api/4/news/latest")
    Observable<LatestStory> getLatest();

    //http://news-at.zhihu.com/api/4/news/8391488
    @GET("api/4/news/{id}")
    Observable<Content> getContent(@Path("id") String id);

    //http://news.at.zhihu.com/api/4/news/before/20131119
    @GET("api/4/news/before/{date}")
    Observable<BeforeStroy> getBefore(@Path("date") String date);
}
