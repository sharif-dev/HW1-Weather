package com.sharifdev.weather.models.weather;

import com.google.gson.annotations.SerializedName;

public class Weather {
    @SerializedName("maxtemp_c")
    private float maxTemperature;
    @SerializedName("mintemp_c")
    private float minTemperature;
    @SerializedName("avgtemp_c")
    private float avgTemperature;
    @SerializedName("maxwind_kph")
    private float maxWindSpeed;
    @SerializedName("avghumidity")
    private float avgHumidity;
    @SerializedName("condition")
    private Condition condition;

    public float getMaxTemperature() {
        return maxTemperature;
    }

    public void setMaxTemperature(float maxTemperature) {
        this.maxTemperature = maxTemperature;
    }

    public float getMinTemperature() {
        return minTemperature;
    }

    public void setMinTemperature(float minTemperature) {
        this.minTemperature = minTemperature;
    }

    public float getAvgTemperature() {
        return avgTemperature;
    }

    public void setAvgTemperature(float avgTemperature) {
        this.avgTemperature = avgTemperature;
    }

    public float getMaxWindSpeed() {
        return maxWindSpeed;
    }

    public void setMaxWindSpeed(float maxWindSpeed) {
        this.maxWindSpeed = maxWindSpeed;
    }

    public float getAvgHumidity() {
        return avgHumidity;
    }

    public void setAvgHumidity(float avgHumidity) {
        this.avgHumidity = avgHumidity;
    }

    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }
}
