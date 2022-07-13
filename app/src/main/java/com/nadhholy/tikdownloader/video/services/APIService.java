package com.nadhholy.tikdownloader.video.services;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;


public class APIService extends APIServiceH {

    private static APIService service = null;

    private APIService(){

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client.build())
                .build();

    }


    public static APIService getInstance(){

        if (service == null) {
            service = new APIService();
        }
        return service;

    }

}
