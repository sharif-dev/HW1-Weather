package com.sharifdev.weather;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sharifdev.weather.models.weather.WeatherResponse;
import com.sharifdev.weather.network.RetrofitClient;
import com.sharifdev.weather.network.WeatherAPI;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {
    Calendar calendar = Calendar.getInstance();
    TextView temperatureText;

    double longitude = 51.388973;
    double latitude = 35.689198;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        temperatureText = findViewById(R.id.text_temperature);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LocationActivity.class);
                startActivity(intent);
            }
        });

        TextView dateText = findViewById(R.id.text_date);
        Date now = calendar.getTime();
        String pattern = "EEE, MMM d HH:mm";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String date = simpleDateFormat.format(now);
        dateText.setText(date);


        final RetrofitClient weatherClient = new RetrofitClient(getString(R.string.weather_api));
        final WeatherAPI service = weatherClient.getRetrofit().create(WeatherAPI.class);

        List<Double> coordinates = new ArrayList<>();
        coordinates.add(longitude);
        coordinates.add(latitude);

        Collections.reverse(coordinates);
        Call<WeatherResponse> call = service.getWeather(
                coordinates,
                getString(R.string.weather_token));

        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                WeatherResponse weatherResponse = response.body();
                System.out.println(response);
//                temperatureText.setText("");
                float temperature = weatherResponse.getCurrentSummeryWeather().getTemperature();
                temperatureText.setText(String.format("%.1f°", temperature));
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                Log.e("weather", Objects.requireNonNull(t.getMessage()));
            }
        });



    }
}
