package com.sharifdev.weather.models.weather;

import com.google.gson.annotations.SerializedName;

public class WeatherDay {
    @SerializedName("date")
    private String date;
    @SerializedName("day")
    private Weather weather;
    @SerializedName("astro")
    private AstronomicalInformation astronomicalInformation;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Weather getWeather() {
        return weather;
    }

    public void setWeather(Weather weather) {
        this.weather = weather;
    }

    public AstronomicalInformation getAstronomicalInformation() {
        return astronomicalInformation;
    }

    public void setAstronomicalInformation(AstronomicalInformation astronomicalInformation) {
        this.astronomicalInformation = astronomicalInformation;
    }
}
