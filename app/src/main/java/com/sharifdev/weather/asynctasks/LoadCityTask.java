package com.sharifdev.weather.asynctasks;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.sharifdev.weather.R;
import com.sharifdev.weather.datamodels.CityDataCallback;
import com.sharifdev.weather.models.coordination.City;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;

public class LoadCityTask extends AsyncTask<Void, Void, City> {
    @SuppressLint("StaticFieldLeak")
    private Context context;
    private CityDataCallback callback;

    public LoadCityTask(Context context, CityDataCallback callback) {
        this.context = context;
        this.callback = callback;
    }

    @Override
    protected City doInBackground(Void... voids) {
        try {
            InputStreamReader inputStreamReader =
                    new InputStreamReader(
                            context.openFileInput(
                                    context.getString(R.string.city_file_name)));
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
            return gson.fromJson(json, City.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(City city) {
        this.callback.onComplete(Collections.singletonList(city));
    }
}
