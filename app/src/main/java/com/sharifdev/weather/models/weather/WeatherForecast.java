package com.sharifdev.weather.models.weather;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WeatherForecast {
    @SerializedName("forecastday")
    private List<WeatherDay> weatherDay;

    public List<WeatherDay> getWeatherDay() {
        return weatherDay;
    }
}
