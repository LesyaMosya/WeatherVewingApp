package com.example.weathervewingapp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

public class CurrentWeatherResponse {
    @SerializedName("weather")
    @Expose
    private List<WeatherJSON> weather;
    @SerializedName("main")
    @Expose
    private MainDataJSON main;
    @SerializedName("wind")
    @Expose
    private WindJSON wind;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("dt_txt")
    @Expose
    private Date dt_txt;


    public List<WeatherJSON> getWeather() {
        return weather;
    }

    public MainDataJSON getMain() {
        return main;
    }

    public WindJSON getWind() {
        return wind;
    }

    public String getName() {
        return name;
    }

    public Date getDt_txt() {
        return dt_txt;
    }
}
