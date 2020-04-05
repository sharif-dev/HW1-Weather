package com.sharifdev.weather;

import android.os.Bundle;
import android.telephony.mbms.MbmsErrors;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.sharifdev.weather.models.coordination.CitiesResponse;
import com.sharifdev.weather.models.coordination.City;
import com.sharifdev.weather.models.weather.WeatherResponse;
import com.sharifdev.weather.network.Mapbox;
import com.sharifdev.weather.network.RetrofitClient;
import com.sharifdev.weather.network.WeatherAPI;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LocationActivity extends AppCompatActivity {
    private CityAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        List<City> citiesList = new ArrayList<>();

        final ListView listview = findViewById(R.id.listview);
        EditText Search = findViewById(R.id.text_city_search);

        adapter = new CityAdapter(this, citiesList);
        listview.setAdapter(adapter);

        RetrofitClient mapboxClient = new RetrofitClient(getString(R.string.mapbox_api));
        final Mapbox service = mapboxClient.getRetrofit().create(Mapbox.class);

        Search.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                String[] cities;
                if (cs.length() != 0) {
                    // Getting cities list from mapbox api
                    Call<CitiesResponse> call = service.getCities(cs.toString(), getString(R.string.mapbox_token));
                    call.enqueue(new Callback<CitiesResponse>() {
                        @Override
                        public void onResponse(Call<CitiesResponse> call, Response<CitiesResponse> response) {
                            List<City> citiesList = response.body().getCities();
                            for (City city : citiesList) {
                                System.out.print(city.getName() + "  ");
                                System.out.println(city.getCoordinates());
                            }
                            adapter.setCities(citiesList);
                            adapter.notifyDataSetChanged();

                        }

                        @Override
                        public void onFailure(Call<CitiesResponse> call, Throwable t) {
                            System.err.println(t.getMessage());
                        }
                    });
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                City clickedCity = (City) listview.getAdapter().getItem(position);

                TextView currentCity = findViewById(R.id.text_current_location);
                TextView latitude = findViewById(R.id.city_text_latitude);
                TextView longitude = findViewById(R.id.city_text_longitude);

                currentCity.setText(clickedCity.getRealName());
                latitude.setText(String.format("Latitude: %f, ", clickedCity.getCoordinates().get(1)));
                longitude.setText(String.format("Longitude: %f, ", clickedCity.getCoordinates().get(0)));


                final RetrofitClient weatherClient = new RetrofitClient(getString(R.string.weather_api));
                final WeatherAPI service = weatherClient.getRetrofit().create(WeatherAPI.class);

                List<Double> coordinates = clickedCity.getCoordinates();
                Collections.reverse(coordinates);
                Call<WeatherResponse> call = service.getWeather(
                        coordinates,
                        getString(R.string.weather_token));

                call.enqueue(new Callback<WeatherResponse>() {
                    @Override
                    public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                        WeatherResponse weatherResponse = response.body();
                        System.out.print(weatherResponse.getLocation().getName() + "  ");
                        System.out.println(weatherResponse.getCurrentSummeryWeather().getTemperature());
                        System.out.println(weatherResponse.getCurrentSummeryWeather().getCondition());
                    }

                    @Override
                    public void onFailure(Call<WeatherResponse> call, Throwable t) {
                        Log.e("weather", Objects.requireNonNull(t.getMessage()));
                    }
                });

                // TODO: Save selected City
            }
        });
    }

}
