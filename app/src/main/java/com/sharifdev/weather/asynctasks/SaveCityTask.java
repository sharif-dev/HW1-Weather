package com.sharifdev.weather.asynctasks;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.sharifdev.weather.R;
import com.sharifdev.weather.datamodels.CityDataCallback;
import com.sharifdev.weather.models.coordination.City;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;


public class SaveCityTask extends AsyncTask<City, Void, Integer> {
    @SuppressLint("StaticFieldLeak")
    private Context context;
    private CityDataCallback callback;
    private City city;

    public SaveCityTask(Context context, CityDataCallback callback) {
        this.context = context;
        this.callback = callback;
    }

    @Override
    protected Integer doInBackground(City... cities) {
        Gson gson = new Gson();
        City city = cities[0];
        this.city = city;
        String json = gson.toJson(city);
        FileOutputStream outputStream;
        try {
            outputStream = context.getApplicationContext().openFileOutput(
                    context.getString(R.string.city_file_name),
                    Context.MODE_PRIVATE);
            outputStream.write(json.getBytes());
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 200;
    }

    @Override
    protected void onPostExecute(Integer integer) {
        this.callback.onComplete(Collections.singletonList(city));
    }
}
