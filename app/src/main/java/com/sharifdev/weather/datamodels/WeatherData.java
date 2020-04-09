package com.sharifdev.weather.datamodels;

import android.content.Context;
import android.util.Log;

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
    private static WeatherData instance = new WeatherData();
    private WeatherResponse weather;
    private Context context;
    String TAG = "Weather Data";
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
            callback.onComplete(weatherResponse);
        } catch (DataNotUpdateException ignored) { }

        if(internetConnection) {
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
        }
        ArrayList<City> cities = new ArrayList<>();
    }

    public WeatherResponse loadWeather(WeatherDataCallback callback) throws DataNotUpdateException {

        WeatherResponse weatherResponse = null;

        // check if previous data exists for this city
        LoadWeatherTask loadWeatherTask = new LoadWeatherTask(this.context, callback);
        String lastSavedDataTime ;
        try {
            // try for reading last saved data
            weatherResponse = loadWeatherTask.execute().get();
            lastSavedDataTime = weatherResponse.getCurrentSummeryWeather().getLastUpdated();
        } catch (ExecutionException | InterruptedException | NullPointerException e) {
            // data not found
            Log.d(TAG, "Weather data file is not saved yet.");
            e.printStackTrace();
            throw  new DataNotUpdateException();
        }
        Log.i(TAG, "last updated is time " + lastSavedDataTime);
        // check if data is update
        if (isTooOld(lastSavedDataTime)) {
            Log.d(TAG, "Weather data file was too old to use again.");
            throw  new DataNotUpdateException();
        }
        Log.d(TAG, "Weather data used from file.");
        return weatherResponse;
    }

    private boolean isTooOld(String lastSavedDataTime) {

        // format the string of time
        String format = "yyyy-MM-dd hh:mm";
        SimpleDateFormat simpleDateFormatter = new SimpleDateFormat(format);
        try {
            Date lastUpdateTime = simpleDateFormatter.parse(lastSavedDataTime);
            Date now = Calendar.getInstance().getTime();
            int diffhours = (int) ((lastUpdateTime.getTime() - now.getTime()) / (60 * 1000));

            // check if last update is too old
            if (diffhours > OLDNESS_HOUR_CONST) {
                return true;
            }
        } catch (ParseException ignored) { }
        return false;

    }

    public void saveWeather(WeatherResponse weather) {
        SaveWeatherTask saveWeatherTask = new SaveWeatherTask(this.context);
        saveWeatherTask.execute(weather);
    }

    class DataNotUpdateException extends Exception {
        public DataNotUpdateException() {
            super("Last saved data is not updated.");
        }
    }

}
