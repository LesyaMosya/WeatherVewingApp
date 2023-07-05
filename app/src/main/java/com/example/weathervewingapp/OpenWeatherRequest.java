package com.example.weathervewingapp;

import com.google.gson.annotations.SerializedName;


public class OpenWeatherRequest {
    @SerializedName("appid")
    private final String appid = "32d6218651879bf8d4e4e888b784551f";
    @SerializedName("units")
    private final String units = "metric";
    @SerializedName("lat")
    private double lat = 59.9386;
    @SerializedName("lon")
    private double lon = 30.3141;
    @SerializedName("lang")
    private String lang = "ru";


    public String getAppid(){
        return appid;
    }

    public String getUnits(){
        return units;
    }

    public double getLat(){
        return lat;
    }
    public void setLat(double lat){
        this.lat=lat;
    }

    public double getLon(){
        return lon;
    }
    public void setLon(double lon){
        this.lon=lon;
    }

    public String getLang(){
        return lang;
    }
    public void setLang(String lang){
        this.lang=lang;
    }
}
