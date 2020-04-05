package com.sharifdev.weather.models.weather;

import com.google.gson.annotations.SerializedName;

class AstronomicalInformation {
    @SerializedName("sunrise")
    private String sunrise;
    @SerializedName("sunset")
    private String sunset;

    public String getSunrise() {
        return sunrise;
    }

    public void setSunrise(String sunrise) {
        this.sunrise = sunrise;
    }

    public String getSunset() {
        return sunset;
    }

    public void setSunset(String sunset) {
        this.sunset = sunset;
    }
}
