package com.sharifdev.weather.network;


import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Mapbox {
    @GET("{query}.json")
    Call<JsonObject> getWeather(@Path("query") String query, @Query("access_token") String token);
}
