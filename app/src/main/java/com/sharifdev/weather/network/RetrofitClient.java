package com.sharifdev.weather.network;

import okhttp3.HttpUrl;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private Retrofit retrofit;

    public RetrofitClient(String baseUrl) {
        retrofit = new retrofit2.Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }
}
