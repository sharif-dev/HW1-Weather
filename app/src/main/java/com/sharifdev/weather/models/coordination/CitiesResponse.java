package com.sharifdev.weather.models.coordination;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CitiesResponse {
    @SerializedName("features")
    private List<City> cities;

    public List<City> getCities() {
        return cities;
    }

    public void setCities(List<City> cities) {
        this.cities = cities;
    }
}
