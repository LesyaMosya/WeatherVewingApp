package com.example.weathervewingapp;

import com.google.gson.annotations.SerializedName;


public class OpenWeatherRequest {
    @SerializedName("appid")
    private static final String appid = "32d6218651879bf8d4e4e888b784551";
    @SerializedName("units")
    private final String units = "metric";
    @SerializedName("lat")
    private float lat;
    @SerializedName("lon")
    private float lon;
    @SerializedName("lang")
    private String lang;

    public OpenWeatherRequest(float lat, float lon, String lang) {
        this.lat = lat;
        this.lon = lon;
        this.lang = lang;
    }
}
