package com.sharifdev.weather.datamodels;

import android.content.Context;

import com.sharifdev.weather.asynctasks.LoadCityTask;
import com.sharifdev.weather.asynctasks.SaveCityTask;
import com.sharifdev.weather.models.coordination.CitiesResponse;
import com.sharifdev.weather.models.coordination.City;
import com.sharifdev.weather.network.Mapbox;
import com.sharifdev.weather.network.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CityData {
    private Context context;
    private static CityData instance = new CityData();

    public static CityData getInstance(Context context) {
        instance.context = context;
        return instance;
    }

    public void loadCity(CityDataCallback callback) {
        LoadCityTask loadCityTask = new LoadCityTask(this.context, callback);
        loadCityTask.execute();
    }

    public void saveCity(final City city, CityDataCallback callback) {
        SaveCityTask saveCityTask = new SaveCityTask(this.context, callback);
        saveCityTask.execute(city);
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
