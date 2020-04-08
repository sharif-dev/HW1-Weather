package com.sharifdev.weather.network;

import com.sharifdev.weather.models.coordination.CitiesResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Mapbox {
    @GET("{query}.json")
    Call<CitiesResponse> getCities(@Path("query") String query, @Query("access_token") String token);
}
