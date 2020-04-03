package com.sharifdev.weather;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sharifdev.weather.models.CitiesResponse;
import com.sharifdev.weather.models.City;
import com.sharifdev.weather.network.Mapbox;
import com.sharifdev.weather.network.RetrofitClient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import android.widget.Button;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {

    Button weather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        weather = findViewById(R.id.getWeatherBotton);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LocationActivity.class);
                startActivity(intent);
            }
        });

        weather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RetrofitClient mapboxClient = new RetrofitClient(getString(R.string.mapbox_url));

                Mapbox service = mapboxClient.getRetrofit().create(Mapbox.class);
                Call<CitiesResponse> call = service.getWeather("teh", getString(R.string.mapbox_token));
                call.enqueue(new Callback<CitiesResponse>() {
                    @Override
                    public void onResponse(Call<CitiesResponse> call, Response<CitiesResponse> response) {
                        List<City> cities = response.body().getCities();
                        for (City city: cities) {
                            System.out.print(city.getName() + "  ");
                            System.out.println(city.getCoordinates());
                        }
                    }

                    @Override
                    public void onFailure(Call<CitiesResponse> call, Throwable t) {
                        System.err.println(t.getMessage());
                    }
                });
            }
        });
    }
}
