package com.sharifdev.weather.datamodels;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sharifdev.weather.R;
import com.sharifdev.weather.asynctasks.LoadWeatherTask;
import com.sharifdev.weather.asynctasks.SaveWeatherTask;
import com.sharifdev.weather.models.coordination.City;
import com.sharifdev.weather.models.weather.WeatherResponse;
import com.sharifdev.weather.network.InternetConnectionChecker;
import com.sharifdev.weather.network.RetrofitClient;
import com.sharifdev.weather.network.WeatherAPI;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherData {
    @SuppressLint("StaticFieldLeak")
    private static WeatherData instance = new WeatherData();
    private WeatherResponse weather;
    private Context context;
    private static final String TAG = "WeatherData";
    private final int OLDNESS_HOUR_CONST = 1;

    private WeatherData() {
    }

    public static WeatherData getInstance(Context context) {
        instance.context = context;
        return instance;
    }

    public void getWeatherData(City city, String weatherAPI, String apiToken, final WeatherDataCallback callback) {
        boolean internetConnection = InternetConnectionChecker.checkConnection(context);

        try {
            WeatherResponse weatherResponse = loadWeather(callback);
            Log.v(TAG, weatherResponse.getLocation().getCounty() + " / " + city.getCountry());
            if (city.equals(weatherResponse.getLocation().getCounty())) {
                callback.onComplete(weatherResponse);
                Log.d(TAG, "Weather data used from file.");
                return;
            }
        } catch (DataNotUpdateException ignored) {
        }

        Log.d(TAG, "Requesting updated data from api ...");

        if (internetConnection) {
            final RetrofitClient weatherClient = new RetrofitClient(weatherAPI);
            final WeatherAPI service = weatherClient.getRetrofit().create(WeatherAPI.class);

            Call<WeatherResponse> call = service.getWeather(
                    city.getCoordinates(),
                    apiToken);

            call.enqueue(new Callback<WeatherResponse>() {
                @Override
                public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                    try {
                        response.body().getLocation().getName();
                        response.body().getCurrentSummeryWeather().getHumidity();
                        response.body().getWeatherForecast().getWeatherDay();
                    } catch (NullPointerException e) {
                        callback.onFailure(new Throwable(context.getString(R.string.loction_not_supported)));
                        return;
                    }
                    callback.onComplete(response.body());
                    instance.saveWeather(response.body());
                }

                @Override
                public void onFailure(Call<WeatherResponse> call, Throwable t) {
                    callback.onFailure(t);
                }
            });
        }
        ArrayList<City> cities = new ArrayList<>();
    }

    private WeatherResponse loadWeather(WeatherDataCallback callback) throws DataNotUpdateException {

        WeatherResponse weatherResponse = null;

        // check if previous data exists for this city
        LoadWeatherTask loadWeatherTask = new LoadWeatherTask(this.context, callback);
        String lastSavedDataTime;
        String lastCity;
        try {
            // try for reading last saved data
            weatherResponse = loadWeatherTask.execute().get();
            lastSavedDataTime = weatherResponse.getCurrentSummeryWeather().getLastUpdated();
            lastCity = weatherResponse.getLocation().getName();
        } catch (ExecutionException | InterruptedException | NullPointerException e) {
            // data not found
            Log.d(TAG, "Weather data file is not saved yet.");
            e.printStackTrace();
            throw new DataNotUpdateException();
        }
        Log.i(TAG, "last updated is for city " + lastCity + " in time " + lastSavedDataTime);
        // check if data is update
        if (isTooOld(lastSavedDataTime)) {
            Log.d(TAG, "Weather data file was too old to use again.");
            throw new DataNotUpdateException();
        }
        return weatherResponse;
    }

    private boolean isTooOld(String lastSavedDataTime) {

        // format the string of time
        String format = "yyyy-MM-dd hh:mm";
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat simpleDateFormatter = new SimpleDateFormat(format);
        try {
            Date lastUpdateTime = simpleDateFormatter.parse(lastSavedDataTime);
            Date now = Calendar.getInstance().getTime();

            // Unit is hour
            assert lastUpdateTime != null;
            int diffhours = (int) ((lastUpdateTime.getTime() - now.getTime()) / (60 * 60 * 1000));

            // check if last update is too old
            if (diffhours > OLDNESS_HOUR_CONST) {
                return true;
            }
        } catch (ParseException ignored) {
        }
        return false;

    }

    private void saveWeather(WeatherResponse weather) {
        SaveWeatherTask saveWeatherTask = new SaveWeatherTask(this.context);
        saveWeatherTask.execute(weather);
    }

    static class DataNotUpdateException extends Exception {
        DataNotUpdateException() {
            super("Last saved data is not updated.");
        }
    }

}
