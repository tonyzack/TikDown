package com.nadhholy.tikdownloader.video.services;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;


public class APIServiceH {

    //    public static final String BASE_URL = "https://momo.co";
    public static final String BASE_URL = "http://192.168.1.64:9000";


    Retrofit retrofit = null;

    protected OkHttpClient.Builder client = new OkHttpClient.Builder()
            .connectTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .readTimeout(40, TimeUnit.SECONDS);


    APIServiceH(){

        client.addInterceptor(chain -> {
            Request request = chain.request().newBuilder()
                    .addHeader("Accept-Language", Locale.getDefault().getLanguage())
                    .build();
            return chain.proceed(request);
        });
    }


    public DataService getDataService() {
        return retrofit.create(DataService.class);
    }

}

