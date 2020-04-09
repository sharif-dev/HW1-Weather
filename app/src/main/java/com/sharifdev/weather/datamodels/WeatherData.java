package com.sharifdev.weather.datamodels;

import android.content.Context;

import com.sharifdev.weather.asynctasks.SaveWeatherTask;
import com.sharifdev.weather.models.coordination.City;
import com.sharifdev.weather.models.weather.WeatherResponse;
import com.sharifdev.weather.network.RetrofitClient;
import com.sharifdev.weather.network.WeatherAPI;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherData {
    private static WeatherData instance = new WeatherData();
    private WeatherResponse weather = new WeatherResponse();
    private Context context;

    private WeatherData() {
    }

    public static WeatherData getInstance() {
        return instance;
    }

    public void loadWeatherData(City city, String weatherAPI, String apiToken, final WeatherDataCallback callback) {
        // TODO: refactor       @mhbahmani
        // TODO: read from file in another thread if exists

        final RetrofitClient weatherClient = new RetrofitClient(weatherAPI);
        final WeatherAPI service = weatherClient.getRetrofit().create(WeatherAPI.class);

        Call<WeatherResponse> call = service.getWeather(
                city.getCoordinates(),
                apiToken);

        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                callback.onComplete(response.body());
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                callback.onFailure(t);
            }
        });

        ArrayList<City> cities = new ArrayList<>();
    }

    public void saveWeatherData(WeatherResponse weather) {
        final AtomicReference<SaveWeatherTask> saveWeatherTask = new AtomicReference<>();
        saveWeatherTask.getAndSet(new SaveWeatherTask(this.context));
        saveWeatherTask.get().execute(weather);
        System.out.println("saved");
    }

}
