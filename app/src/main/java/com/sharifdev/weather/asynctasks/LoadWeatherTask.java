package com.sharifdev.weather.asynctasks;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.sharifdev.weather.R;
import com.sharifdev.weather.datamodels.WeatherDataCallback;
import com.sharifdev.weather.models.weather.WeatherResponse;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class LoadWeatherTask extends AsyncTask<Void, Void, WeatherResponse> {
    @SuppressLint("StaticFieldLeak")
    private Context context;
    private WeatherDataCallback callback;


    public LoadWeatherTask(Context context, WeatherDataCallback callback) {
        this.context = context;
        this.callback = callback;
    }

    @Override
    protected WeatherResponse doInBackground(Void... voids) {
        try {
            InputStreamReader inputStreamReader =
                    new InputStreamReader(
                            context.openFileInput(
                                    context.getString(R.string.weather_file_name)));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder stringBuilder = new StringBuilder();
            String string;

            while ((string = bufferedReader.readLine()) != null) {
                stringBuilder.append(string).append("\n");
            }
            inputStreamReader.close();
            bufferedReader.close();
            String json = stringBuilder.toString();
            Gson gson = new Gson();
            return gson.fromJson(json, WeatherResponse.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
