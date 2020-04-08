package com.sharifdev.weather.models.weather;

import com.google.gson.annotations.SerializedName;

import java.util.List;

class WeatherForecast {
    @SerializedName("forecastday")
    private List<WeatherDay> weatherDay;

    public List<WeatherDay> getWeather() {
        return weatherDay;
    }

    public void setWeather(List<WeatherDay> weather) {
        this.weatherDay = weatherDay;
    }
}
