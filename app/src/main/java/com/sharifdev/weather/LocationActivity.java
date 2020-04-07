package com.sharifdev.weather;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.sharifdev.weather.datamodels.CityData;
import com.sharifdev.weather.datamodels.CityDataCallback;
import com.sharifdev.weather.models.coordination.City;

import java.util.Collections;
import java.util.List;


public class LocationActivity extends AppCompatActivity {
    private CityAdapter adapter;
    ListView searchResultListView;
    EditText citySearchText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        searchResultListView = findViewById(R.id.listview);
        citySearchText = findViewById(R.id.text_city_search);

        adapter = new CityAdapter(this, Collections.<City>emptyList());
        searchResultListView.setAdapter(adapter);

        citySearchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                String query = cs.toString();
                if (cs.length() == 0) {
                    adapter.setCities(Collections.<City>emptyList());
                    adapter.notifyDataSetChanged();
                } else {
                    CityData.getInstance().searchCities(
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
                City clickedCity = (City) searchResultListView.getAdapter().getItem(position);

                TextView currentCity = findViewById(R.id.text_current_location);
                TextView latitude = findViewById(R.id.city_text_latitude);
                TextView longitude = findViewById(R.id.city_text_longitude);

                currentCity.setText(clickedCity.getRealName());
                latitude.setText(String.format("Latitude: %f, ", clickedCity.getCoordinates().get(1)));
                longitude.setText(String.format("Longitude: %f, ", clickedCity.getCoordinates().get(0)));


                // TODO: Save selected City
            }
        });
    }

}
