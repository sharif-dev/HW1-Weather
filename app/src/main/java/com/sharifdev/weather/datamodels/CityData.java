package com.sharifdev.weather.datamodels;

import android.content.Context;

import com.sharifdev.weather.asynctasks.LoadCityTask;
import com.sharifdev.weather.models.coordination.CitiesResponse;
import com.sharifdev.weather.models.coordination.City;
import com.sharifdev.weather.network.Mapbox;
import com.sharifdev.weather.network.RetrofitClient;
import com.sharifdev.weather.asynctasks.SaveCityTask;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicReference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CityData {
    private Context context;
    private static CityData instance = new CityData();
    private City defaultCity;

    private CityData() {
        defaultCity = new City();
        defaultCity.setName("Tehran");
        defaultCity.setCoordinates(new ArrayList<Double>());
        defaultCity.getCoordinates().add(51.388973);
        defaultCity.getCoordinates().add(35.689198);
    }

    public static CityData getInstance(Context context) {
        instance.context = context;
        return instance;
    }

    public void getCity(CityDataCallback callback) {
        final AtomicReference<LoadCityTask> loadCityTask = new AtomicReference<>();
        loadCityTask.getAndSet(new LoadCityTask(this.context));
        loadCityTask.get().execute();

        callback.onComplete(Collections.singletonList(defaultCity));
    }

    public void setCity(final City city, CityDataCallback callback) {
        final AtomicReference<SaveCityTask> saveCityTask = new AtomicReference<>();
        saveCityTask.getAndSet(new SaveCityTask(this.context));
        saveCityTask.get().execute(city);

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
