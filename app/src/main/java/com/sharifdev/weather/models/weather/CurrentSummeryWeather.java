package com.sharifdev.weather.models.weather;

import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CurrentSummeryWeather {
    @SerializedName("last_updated")
    private String lastUpdated;
    @SerializedName("temp_c")
    private float temperature;
    @SerializedName("is_day")
    private int isDay;
    @SerializedName("condition")
    private Condition condition;
    @SerializedName("wind_kph")
    private float windSpeed;
    @SerializedName("wind_dir")
    private String windDirection;
    @SerializedName("humidity")
    private int humidity;
    @SerializedName("feelslike_c")
    private float feelsLike;

    public String getLastUpdated() {
        return lastUpdated;
    }

    public String getLastUpdatedSimple() {
        try {
            Date parsedDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(lastUpdated);
            SimpleDateFormat format = new SimpleDateFormat("EEE, MMM d HH:mm");
            return format.format(parsedDate);
        } catch (ParseException e) {
            return lastUpdated;
        }

    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public float getTemperature() {
        return temperature;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public float getWindSpeed() {
        return windSpeed;
    }

    public int getIsDay() {
        return isDay;
    }

    public void setIsDay(int isDay) {
        this.isDay = isDay;
    }

    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    public void setWindSpeed(float windSpeed) {
        this.windSpeed = windSpeed;
    }

    public String getWindDirection() {
        return windDirection;
    }

    public void setWindDirection(String windDirection) {
        this.windDirection = windDirection;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public float getFeelsLike() {
        return feelsLike;
    }

    public void setFeelsLike(float feelsLike) {
        this.feelsLike = feelsLike;
    }
}
