package com.sharifdev.weather.datamodels;

import com.sharifdev.weather.models.coordination.City;

public interface CityDataCallback {
    void onComplete(City data);

    void onFailure(Throwable t);
}
