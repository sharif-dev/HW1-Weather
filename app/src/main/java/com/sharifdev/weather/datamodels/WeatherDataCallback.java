package com.sharifdev.weather.datamodels;

import com.sharifdev.weather.models.weather.WeatherResponse;

public interface WeatherDataCallback {
    void onComplete(WeatherResponse data);

    void onFailure();
}
