package com.sharifdev.weather;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.sharifdev.weather.asynctasks.WeatherIconTask;
import com.sharifdev.weather.datamodels.CityData;
import com.sharifdev.weather.datamodels.CityDataCallback;
import com.sharifdev.weather.datamodels.WeatherData;
import com.sharifdev.weather.datamodels.WeatherDataCallback;
import com.sharifdev.weather.models.coordination.City;
import com.sharifdev.weather.models.weather.WeatherResponse;
import com.sharifdev.weather.network.InternetConnectionChecker;

import java.util.List;
import java.util.Objects;


public class MainActivity extends AppCompatActivity {
    ImageView conditionIcon;
    TextView temperatureText;
    TextView textCity;
    TextView dateText;
    TextView weatherStatusText;
    ProgressBar progressBar;
    View content;
    City city;
    int[] forecastImageIds;
    int[] forecastLowIds;
    int[] forecastDateIds;
    int[] forecastHighIds;
    int forecastCount = 5;
    int shortAnimationDuration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        temperatureText = findViewById(R.id.text_temperature);
        conditionIcon = findViewById(R.id.condition_icon);
        textCity = findViewById(R.id.text_city);
        dateText = findViewById(R.id.text_date);
        weatherStatusText = findViewById(R.id.weather_status);
        content = findViewById(R.id.content);
        progressBar = findViewById(R.id.progress_bar);
        shortAnimationDuration = getResources().getInteger(android.R.integer.config_shortAnimTime);


        forecastImageIds = new int[]{
                R.id.forecast_condition_0,
                R.id.forecast_condition_1,
                R.id.forecast_condition_2,
                R.id.forecast_condition_3,
                R.id.forecast_condition_4,
        };

        forecastDateIds = new int[]{
                R.id.forecast_date_0,
                R.id.forecast_date_1,
                R.id.forecast_date_2,
                R.id.forecast_date_3,
                R.id.forecast_date_4,
        };

        forecastLowIds = new int[]{
                R.id.forecast_low_0,
                R.id.forecast_low_1,
                R.id.forecast_low_2,
                R.id.forecast_low_3,
                R.id.forecast_low_4,
        };

        forecastHighIds = new int[]{
                R.id.forecast_high_0,
                R.id.forecast_high_1,
                R.id.forecast_high_2,
                R.id.forecast_high_3,
                R.id.forecast_high_4,
        };

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (InternetConnectionChecker.checkConnection(getApplicationContext())) {
                    Intent intent = new Intent(MainActivity.this, LocationActivity.class);
                    startActivity(intent);
                } else {
                    Snackbar.make(
                            view,
                            getString(R.string.no_internet_connection),
                            Snackbar.LENGTH_LONG).show();
                }
            }
        });
        content.setVisibility(View.GONE);

        refresh();
    }

    @Override
    public void onResume() {
        super.onResume();
        refresh();
    }

    public void refresh() {
        content.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        CityData.getInstance(getApplicationContext()).loadCity(new CityDataCallback() {
            @Override
            public void onComplete(List<City> cities) {
                city = cities.get(0);
                if (city == null) {
                    Intent intent = new Intent(MainActivity.this, LocationActivity.class);
                    startActivity(intent);
                } else {
                    textCity.setText(city.getRealName());
                    WeatherData.getInstance(getApplicationContext()).getWeatherData(
                            city,
                            getString(R.string.weather_api),
                            getString(R.string.weather_token),
                            new WeatherDataCallback() {
                                @Override
                                public void onComplete(WeatherResponse data) {
                                    float temperature = data.getCurrentSummeryWeather().getTemperature();
                                    temperatureText.setText(String.format("%.1f°", temperature));
                                    weatherStatusText.setText(data.getCurrentSummeryWeather().getCondition().getCondition());
                                    dateText.setText(data.getCurrentSummeryWeather().getLastUpdatedSimple());

                                    for (int i = 0; i < forecastCount; i++) {
                                        TextView date = findViewById(forecastDateIds[i]);
                                        TextView low = findViewById(forecastLowIds[i]);
                                        TextView high = findViewById(forecastHighIds[i]);
                                        ImageView forecastCondition = findViewById(forecastImageIds[i]);

                                        date.setText(data.getWeatherForecast().getWeatherDay().get(i).getDateSimple());
                                        float minTemperature = data.getWeatherForecast().getWeatherDay().get(i).getWeather().getMinTemperature();
                                        float maxTemperature = data.getWeatherForecast().getWeatherDay().get(i).getWeather().getMaxTemperature();
                                        String minTempStr = String.format("%.1f°", minTemperature);
                                        String maxTempStr = String.format("%.1f°", maxTemperature);
                                        low.setText(minTempStr);
                                        high.setText(maxTempStr);

                                        WeatherIconTask iconTask = new WeatherIconTask(forecastCondition, getResources().getDisplayMetrics().density, 64);
                                        String iconUrl = "http:" + data.getWeatherForecast().getWeatherDay().get(i).getWeather().getCondition().getConditionIconLink();
                                        iconTask.execute(iconUrl);
                                    }

                                    WeatherIconTask iconTask = new WeatherIconTask(conditionIcon, getResources().getDisplayMetrics().density, 64);
                                    String iconUrl = "http:" + data.getCurrentSummeryWeather().getCondition().getConditionIconLink();
                                    iconTask.execute(iconUrl);

                                    content.setVisibility(View.VISIBLE);
                                    progressBar.setVisibility(View.GONE);
                                }

                                @Override
                                public void onFailure(Throwable t) {
                                    Log.e("weather", Objects.requireNonNull(t.getMessage()));
                                    Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
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

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.refresh:
                refresh();
                break;
        }
        return true;
    }
}
