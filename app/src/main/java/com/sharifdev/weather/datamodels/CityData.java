package com.sharifdev.weather.datamodels;

import com.sharifdev.weather.models.coordination.City;

import java.util.ArrayList;

public class CityData {
    private static CityData instance = new CityData();
    private City defaultCity;

    private CityData() {
        defaultCity = new City();
        defaultCity.setName("Tehran");
        defaultCity.setCoordinates(new ArrayList<Double>());
        defaultCity.getCoordinates().add(51.388973);
        defaultCity.getCoordinates().add(35.689198);
    }

    public static CityData getInstance() {
        return instance;
    }

    public void getCity(CityDataCallback callback) {
        // TODO: load city data in another Thread.

        callback.onComplete(defaultCity);
    }

}
