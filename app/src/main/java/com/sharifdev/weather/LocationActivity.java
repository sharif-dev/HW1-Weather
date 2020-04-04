package com.sharifdev.weather;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ListView;

import com.sharifdev.weather.models.CitiesResponse;
import com.sharifdev.weather.models.City;
import com.sharifdev.weather.network.Mapbox;
import com.sharifdev.weather.network.RetrofitClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LocationActivity extends AppCompatActivity {
    private CityAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        String[] cities = {"City1", "City2", "City3", "City4", "City5", "City6", "City7", "City8", "City9", "City10", "City11"};
        List<String> citiesList = new ArrayList<>();
        Collections.addAll(citiesList, cities);

        ListView listview = findViewById(R.id.listview);
        EditText Search = findViewById(R.id.text_city_search);

        adapter = new CityAdapter(this, citiesList);
        listview.setAdapter(adapter);

        RetrofitClient mapboxClient = new RetrofitClient(getString(R.string.mapbox_url));
        final Mapbox service = mapboxClient.getRetrofit().create(Mapbox.class);

        Search.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                String[] cities;
                if (cs.length() != 0) {
                    // Getting cities list from mapbox api
                    Call<CitiesResponse> call = service.getWeather(cs.toString(), getString(R.string.mapbox_token));
                    call.enqueue(new Callback<CitiesResponse>() {
                        @Override
                        public void onResponse(Call<CitiesResponse> call, Response<CitiesResponse> response) {
                            List<City> citiesList = response.body().getCities();
                            for (City city : citiesList) {
                                System.out.print(city.getName() + "  ");
                                System.out.println(city.getCoordinates());
                            }
                        }

                        @Override
                        public void onFailure(Call<CitiesResponse> call, Throwable t) {
                            System.err.println(t.getMessage());
                        }
                    });

                    cities = new String[]{"City1", "City2", "City3", "City4", "City5", "City6"};
                } else {
                    cities = new String[]{"City1", "City2", "City3", "City4", "City5", "City6", "City7", "City8", "City9", "City10", "City11"};
                }
                List<String> citiesList = new ArrayList<>();
                Collections.addAll(citiesList, cities);
                adapter.setCities(citiesList);
                adapter.notifyDataSetChanged();
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
    }

}
