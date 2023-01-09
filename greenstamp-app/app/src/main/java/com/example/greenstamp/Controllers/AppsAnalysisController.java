package com.example.greenstamp.Controllers;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class AppsAnalysisController {
    private static Retrofit retrofitInstance;

    public static Retrofit getInstance() {
        if (retrofitInstance == null) {
            retrofitInstance = new Retrofit.Builder()
                    .baseUrl("http://51.210.255.156:3000/api/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }

        return retrofitInstance;
    }

    private AppsAnalysisController() {
    }
}