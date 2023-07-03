package com.example.weathervewingapp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CurrentWeatherResponse {
    @SerializedName("weather")
    @Expose
    private WeatherJSON weather;
    @SerializedName("main")
    @Expose
    private MainDataJSON main;
    @SerializedName("wind")
    @Expose
    private WindJSON wind;
    @SerializedName("name")
    @Expose
    private String name;


    public WeatherJSON getWeather() {
        return weather;
    }
    public void setWeather(WeatherJSON weather) {
        this.weather = weather;
    }

    public MainDataJSON getMain() {
        return main;
    }
    public void setMain(MainDataJSON main) {
        this.main = main;
    }

    public WindJSON getWind() {
        return wind;
    }
    public void setWind(WindJSON wind) {
        this.wind = wind;
    }

    public String getName() {
        return name;
    }
    public void setSpeed(String name) {
        this.name = name;
    }

}
