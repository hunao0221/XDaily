package com.hugo.xdaily.api;

import com.hugo.xdaily.entry.BeforeStroy;
import com.hugo.xdaily.entry.Content;
import com.hugo.xdaily.entry.LatestStory;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * @author  Hugo
 * Created on 2016/6/2 9:57.
 */
public interface ZhihuApi {
    //最新消息
    @GET("api/4/news/latest")
    Observable<LatestStory> getLatest();

    //日报内容
    @GET("api/4/news/{id}")
    Observable<Content> getContent(@Path("id") String id);

    //过往消息，date是一个日期，如:20160606
    @GET("api/4/news/before/{date}")
    Observable<BeforeStroy> getBefore(@Path("date") String date);
}
