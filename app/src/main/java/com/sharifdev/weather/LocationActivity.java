package com.sharifdev.weather;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.sharifdev.weather.datamodels.CityData;
import com.sharifdev.weather.datamodels.CityDataCallback;
import com.sharifdev.weather.models.coordination.City;
import com.sharifdev.weather.network.InternetConnectionChecker;

import java.util.Collections;
import java.util.List;


public class    LocationActivity extends AppCompatActivity {
    private CityAdapter adapter;
    ListView searchResultListView;
    EditText citySearchText;
    TextView currentCity;
    TextView longitude;
    TextView latitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        searchResultListView = findViewById(R.id.listview);
        citySearchText = findViewById(R.id.text_city_search);
        currentCity = findViewById(R.id.text_current_location);
        latitude = findViewById(R.id.city_text_latitude);
        longitude = findViewById(R.id.city_text_longitude);

        adapter = new CityAdapter(this, Collections.<City>emptyList());
        searchResultListView.setAdapter(adapter);

        CityData.getInstance(getApplicationContext()).loadCity(new CityDataCallback() {
            @SuppressLint({"DefaultLocale", "SetTextI18n"})
            @Override
            public void onComplete(List<City> cities) {
                City city = cities.get(0);
                if (city != null) {
                    currentCity.setText(city.getRealName());
                    latitude.setText(String.format("Latitude: %.2f, ", city.getCoordinates().get(1)));
                    longitude.setText(String.format("Longitude: %.2f, ", city.getCoordinates().get(0)));
                } else {
                    currentCity.setText(R.string.select_city_hint);
                    latitude.setText("");
                    longitude.setText("");
                }
            }

            @Override
            public void onFailure(Throwable t) {
                t.printStackTrace();
            }
        });

        citySearchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                String query = cs.toString();
                if (cs.length() == 0) {
                    adapter.setCities(Collections.<City>emptyList());
                    adapter.notifyDataSetChanged();
                } else {
                    CityData.getInstance(getApplicationContext()).searchCities(
                            query,
                            getString(R.string.mapbox_api),
                            getString(R.string.mapbox_token),
                            new CityDataCallback() {
                                @Override
                                public void onComplete(List<City> cities) {
                                    adapter.setCities(cities);
                                    adapter.notifyDataSetChanged();
                                }

                                @Override
                                public void onFailure(Throwable t) {
                                    t.printStackTrace();
                                }
                            }

                    );
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }
        });

        searchResultListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (InternetConnectionChecker.checkConnection(getApplicationContext())) {
                    City clickedCity = (City) searchResultListView.getAdapter().getItem(position);

                    CityData.getInstance(getApplicationContext()).saveCity(clickedCity, new CityDataCallback() {
                        @Override
                        public void onComplete(List<City> cities) {
                            City savedCity = cities.get(0);

                            currentCity.setText(savedCity.getRealName());
                            latitude.setText(String.format("Latitude: %.2f, ", savedCity.getCoordinates().get(1)));
                            longitude.setText(String.format("Longitude: %.2f, ", savedCity.getCoordinates().get(0)));

                            LocationActivity.this.finish();
                        }

                        @Override
                        public void onFailure(Throwable t) {

                        }
                    });
                } else {
                    Snackbar.make(
                            view,
                            getString(R.string.no_internet_connection),
                            Snackbar.LENGTH_LONG).show();
                }

            }
        });
    }

}
