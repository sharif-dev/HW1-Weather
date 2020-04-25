package com.sharifdev.weather.asynctasks;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.sharifdev.weather.R;
import com.sharifdev.weather.models.weather.WeatherResponse;

import java.io.IOException;
import java.io.OutputStreamWriter;


public class SaveWeatherTask extends AsyncTask<WeatherResponse, Void, Integer> {

    private static final  String TAG = "SaveWeatherTask";

    @SuppressLint("StaticFieldLeak")
    private Context context;

    public SaveWeatherTask(Context context) {
        this.context = context;
    }

    @Override
    protected Integer doInBackground(WeatherResponse... weatherResponses) {
        Gson gson = new Gson();
        WeatherResponse weather = weatherResponses[0];
        String json = gson.toJson(weather);
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.getApplicationContext().openFileOutput(
                    context.getString(R.string.weather_file_name),
                    Context.MODE_PRIVATE));
            outputStreamWriter.write(json);
            System.out.println(json);
            outputStreamWriter.close();
        } catch (IOException e) {
            Log.e(TAG,"File write occurred");
            e.printStackTrace();
        }
        return 200;
    }
}
