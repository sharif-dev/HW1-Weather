package com.sharifdev.weather.network;

import com.sharifdev.weather.models.weather.WeatherResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherAPI {
    @GET("forecast.json")
    Call<WeatherResponse> getWeather(@Query("q") List<Double> q, @Query("key") String key, @Query("days") int days);
}
