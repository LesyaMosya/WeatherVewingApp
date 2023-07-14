package com.example.weathervewingapp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ForecastWeatherResponse {
    @SerializedName("list")
    @Expose
    private List<CurrentWeatherResponse> list;

    public List<CurrentWeatherResponse> getList() {
        return list;
    }
}
