package com.sharifdev.weather.datamodels;

import com.sharifdev.weather.models.coordination.City;

import java.util.List;

public interface CityDataCallback {
    void onComplete(List<City> cities);

    void onFailure(Throwable t);
}
