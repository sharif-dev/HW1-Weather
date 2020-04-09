package com.sharifdev.weather.models.weather;

import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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

    public String getDateSimple() {
        try {
            Date parsedDate = new SimpleDateFormat("yyyy-MM-dd").parse(date);
            SimpleDateFormat format = new SimpleDateFormat("EEE");
            return format.format(parsedDate);
        } catch (ParseException e) {
            return date;
        }
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
