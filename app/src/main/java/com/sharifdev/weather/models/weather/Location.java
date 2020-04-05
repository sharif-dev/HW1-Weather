package com.sharifdev.weather.models.weather;

import com.google.gson.annotations.SerializedName;

public class Location {
    @SerializedName("name")
    private String name;
    @SerializedName("region")
    private String region;
    @SerializedName("country")
    private String county;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }
}
