package com.sharifdev.weather.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class City {
    @SerializedName("place_name")
    private String name;
    @SerializedName("center")
    private List<Double> coordinates;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Double> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<Double> coordinates) {
        this.coordinates = coordinates;
    }
}
