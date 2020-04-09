package com.sharifdev.weather;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sharifdev.weather.asynctasks.WeatherIconTask;
import com.sharifdev.weather.datamodels.CityData;
import com.sharifdev.weather.datamodels.CityDataCallback;
import com.sharifdev.weather.datamodels.WeatherData;
import com.sharifdev.weather.datamodels.WeatherDataCallback;
import com.sharifdev.weather.models.coordination.City;
import com.sharifdev.weather.models.weather.WeatherResponse;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;


public class MainActivity extends AppCompatActivity {
    Calendar calendar = Calendar.getInstance();
    TextView temperatureText;
    ImageView conditionIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        temperatureText = findViewById(R.id.text_temperature);
        conditionIcon = findViewById(R.id.condition_icon);

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

        CityData.getInstance(getApplicationContext()).loadCity(new CityDataCallback() {
            @Override
            public void onComplete(List<City> cities) {
                City city = cities.get(0);
                if (city == null) {
                    Intent intent = new Intent(MainActivity.this, LocationActivity.class);
                    startActivity(intent);
                } else {
                    WeatherData.getInstance().getWeatherData(
                            city,
                            getString(R.string.weather_api),
                            getString(R.string.weather_token),
                            new WeatherDataCallback() {
                                @Override
                                public void onComplete(WeatherResponse data) {
                                    float temperature = data.getCurrentSummeryWeather().getTemperature();
                                    temperatureText.setText(String.format("%.1f°", temperature));

                                    WeatherIconTask iconTask = new WeatherIconTask(conditionIcon, getResources().getDisplayMetrics().density);
                                    String iconUrl = "http:" + data.getCurrentSummeryWeather().getCondition().getConditionIconLink();
                                    iconTask.execute(iconUrl);
                                }

                                @Override
                                public void onFailure(Throwable t) {
                                    Log.e("weather", Objects.requireNonNull(t.getMessage()));
                                }
                            }
                    );
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }
}
