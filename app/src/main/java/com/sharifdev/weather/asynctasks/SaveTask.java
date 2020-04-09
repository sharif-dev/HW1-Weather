package com.sharifdev.weather.asynctasks;

import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.sharifdev.weather.LocationActivity;
import com.sharifdev.weather.MainActivity;
import com.sharifdev.weather.models.coordination.City;

import java.io.FileOutputStream;
import java.io.IOException;
import java.security.AccessControlContext;
import java.util.zip.CheckedOutputStream;

import static java.security.AccessController.getContext;


public class SaveTask extends AsyncTask<City, Void, Integer> {
    private Context context;

    public SaveTask(Context context) {
        this.context = context;
    }

    @Override
    protected Integer doInBackground(City... cities) {
        Gson gson = new Gson();
        City city = cities[0];
        String json = gson.toJson(city);
        FileOutputStream outputStream;
        try {
            outputStream = context.getApplicationContext().openFileOutput(city.getRealName(), Context.MODE_PRIVATE);
            outputStream.write(json.getBytes());
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 200;
    }
}
