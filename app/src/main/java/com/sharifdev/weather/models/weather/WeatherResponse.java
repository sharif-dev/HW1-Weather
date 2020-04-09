package com.sharifdev.weather.models.weather;

import com.google.gson.annotations.SerializedName;

public class WeatherResponse {
    @SerializedName("location")
    private Location location;
    @SerializedName("current")
    private CurrentSummeryWeather currentSummeryWeather;
    @SerializedName("forecast")
    private WeatherForecast weatherForecast;

    public Location getLocation() {
        return location;
    }

    public WeatherForecast getWeatherForecast() {
        return weatherForecast;
    }

    public void setWeatherForecast(WeatherForecast weatherForecast) {
        this.weatherForecast = weatherForecast;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public CurrentSummeryWeather getCurrentSummeryWeather() {
        return currentSummeryWeather;
    }

    public void setCurrentSummeryWeather(CurrentSummeryWeather currentSummeryWeather) {
        this.currentSummeryWeather = currentSummeryWeather;
    }
}