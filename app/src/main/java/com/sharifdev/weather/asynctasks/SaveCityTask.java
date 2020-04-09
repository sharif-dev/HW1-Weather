package com.sharifdev.weather.asynctasks;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.sharifdev.weather.R;
import com.sharifdev.weather.models.coordination.City;

import java.io.FileOutputStream;
import java.io.IOException;


public class SaveCityTask extends AsyncTask<City, Void, Integer> {
    @SuppressLint("StaticFieldLeak")
    private Context context;

    public SaveCityTask(Context context) {
        this.context = context;
    }

    @Override
    protected Integer doInBackground(City... cities) {
        Gson gson = new Gson();
        City city = cities[0];
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
}
