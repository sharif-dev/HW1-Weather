package com.sharifdev.weather.datamodels;

import com.sharifdev.weather.models.coordination.City;

public class WeatherData {
    private static WeatherData instance;

    private WeatherData() {
    }

    public static WeatherData getInstance() {
        return instance;
    }

    public void getWeatherData(City city, WeatherDataCallback callback) {

    }
}
