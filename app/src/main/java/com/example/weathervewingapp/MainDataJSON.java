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

    public float getTemp() {
        return temp;
    }

    public float getFeelsLike() {
        return feelsLike;
    }

    public String getHumidity() {
        return humidity;
    }

}
