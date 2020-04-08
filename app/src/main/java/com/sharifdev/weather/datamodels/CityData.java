package com.sharifdev.weather.datamodels;

import com.sharifdev.weather.models.coordination.CitiesResponse;
import com.sharifdev.weather.models.coordination.City;
import com.sharifdev.weather.network.Mapbox;
import com.sharifdev.weather.network.RetrofitClient;

import java.util.ArrayList;
import java.util.Collections;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CityData {
    private static CityData instance = new CityData();
    private City defaultCity;

    private CityData() {
        defaultCity = new City();
        defaultCity.setName("Tehran");
        defaultCity.setCoordinates(new ArrayList<Double>());
        defaultCity.getCoordinates().add(51.388973);
        defaultCity.getCoordinates().add(35.689198);
    }

    public static CityData getInstance() {
        return instance;
    }

    public void getCity(CityDataCallback callback) {
        // TODO: load city from file data in another Thread.

        callback.onComplete(Collections.singletonList(defaultCity));
    }

    public void setCity(City city, CityDataCallback callback) {
        // TODO: save city in file in another Thread.

        callback.onComplete(Collections.singletonList(city));
    }

    public void searchCities(String query, String api, String token, final CityDataCallback callback) {
        RetrofitClient mapboxClient = new RetrofitClient(api);
        final Mapbox service = mapboxClient.getRetrofit().create(Mapbox.class);

        Call<CitiesResponse> call = service.getCities(query, token);
        call.enqueue(new Callback<CitiesResponse>() {
            @Override
            public void onResponse(Call<CitiesResponse> call, Response<CitiesResponse> response) {
                callback.onComplete(response.body().getCities());
            }

            @Override
            public void onFailure(Call<CitiesResponse> call, Throwable t) {
                callback.onFailure(t);
            }
        });

    }

}
