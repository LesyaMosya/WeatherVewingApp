package com.example.weathervewingapp;

import com.google.gson.annotations.SerializedName;


public class OpenWeatherRequest {
    @SerializedName("appid")
    private final String appid = "32d6218651879bf8d4e4e888b784551f";
    @SerializedName("units")
    private final String units = "metric";
    @SerializedName("cnt")
    private final int cnt = 40;


    public String getAppid(){
        return appid;
    }

    public String getUnits(){
        return units;
    }

    public int getCnt() {
        return cnt;
    }
}
