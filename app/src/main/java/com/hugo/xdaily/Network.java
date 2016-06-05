package com.hugo.xdaily;

import com.hugo.xdaily.api.ZhihuApi;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @auther Hugo
 * Created on 2016/6/2 10:57.
 */
public class Network {

    private static Retrofit retrofit;
    private static ZhihuApi zhihuApi;



    public static ZhihuApi getZhihuApi() {
        if (zhihuApi == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl("http://news-at.zhihu.com/")
                    .client(new OkHttpClient())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
            zhihuApi = retrofit.create(ZhihuApi.class);
        }
        return zhihuApi;
    }


}
