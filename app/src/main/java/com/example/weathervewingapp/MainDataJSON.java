package com.example.weathervewingapp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MainDataJSON {

    @SerializedName("temp")
    @Expose
    private float temp;
    @SerializedName("feels_like")
    @Expose
    private float feelsLike;
    @SerializedName("humidity")
    @Expose
    private String humidity;
    @SerializedName("temp_max")
    @Expose
    private float temp_max;
    @SerializedName("temp_min")
    @Expose
    private float temp_min;

    public float getTemp() {
        return temp;
    }

    public float getFeelsLike() {
        return feelsLike;
    }

    public String getHumidity() {
        return humidity;
    }

    public float getTemp_max() {
        return temp_max;
    }

    public float getTemp_min() {
        return temp_min;
    }
}
