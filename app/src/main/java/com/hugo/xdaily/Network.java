package com.hugo.xdaily;

import com.hugo.xdaily.api.ZhihuApi;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author Hugo
 *         Created on 2016/6/2 10:57.
 */
public class Network {
    private static Retrofit retrofit;
    private static ZhihuApi zhihuApi;
    private static volatile Network instance;

    private Network() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(5, TimeUnit.SECONDS);
        retrofit = new Retrofit.Builder()
                .baseUrl("http://news-at.zhihu.com/")
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        zhihuApi = retrofit.create(ZhihuApi.class);
    }

    private static Network getInstance() {
        if (instance == null) {
            synchronized (Network.class) {
                if (instance == null) {
                    instance = new Network();
                }
            }
        }
        return instance;
    }

    public static ZhihuApi getZhihuApi() {
        getInstance();
        return zhihuApi;
    }
}
