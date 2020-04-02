package com.sharifdev.weather.network;


import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Mapbox {

    String access_token =
            "pk.eyJ1IjoiYWxpc2hpciIsImEiOiJjazhoN2JheDcwOWZ0M25xa2ZvMXIwYjYxIn0.EYQO0NPwPPyO5zHQG8sl3Q";

    @GET("{query}.json")
    Call<JsonObject> getWeather(@Path("query") String query, @Query("access_token") String token);
}
