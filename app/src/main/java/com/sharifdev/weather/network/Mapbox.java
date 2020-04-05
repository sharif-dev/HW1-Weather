package com.sharifdev.weather.network;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.sharifdev.weather.models.CitiesResponse;
import com.sharifdev.weather.models.City;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Mapbox {
    @GET("{query}.json")
    Call<CitiesResponse> getWeather(@Path("query") String query, @Query("access_token") String token);
}
