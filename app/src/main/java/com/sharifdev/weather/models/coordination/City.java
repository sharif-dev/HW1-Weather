package com.sharifdev.weather.models.coordination;

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

    public String getRealName() {
        String[] data = this.getName().split(",\\s+");
        return data[0];
    }

    public void setCoordinates(List<Double> coordinates) {
        this.coordinates = coordinates;
    }

    public String getProvince() {
        String[] data = this.getName().split(",\\s+");
        return data.length > 1 ? data[1] : "";
    }

    public String getCountry() {
        String[] data = this.getName().split(",\\s+");
        return data.length > 2 ? data[2] : "";
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Double> getCoordinates() {
        return coordinates;
    }
}
