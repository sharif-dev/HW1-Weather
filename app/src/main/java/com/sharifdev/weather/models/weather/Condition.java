package com.sharifdev.weather.models.weather;

import com.google.gson.annotations.SerializedName;

import java.net.MalformedURLException;
import java.net.URL;

public class Condition {
    @SerializedName("text")
    private String condition;
    @SerializedName("icon")
    private String conditionIconLink;
    private URL conditionIcon;
    public Condition(String conditionIconLink) {
        try {
            this.conditionIcon = new URL(conditionIconLink.substring(2));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public URL getConditionIcon() {
        return conditionIcon;
    }

    public void setConditionIcon(URL conditionIcon) {
        this.conditionIcon = conditionIcon;
    }
}
