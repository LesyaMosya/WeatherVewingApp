package com.example.weathervewingapp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WindJSON {

    @SerializedName("speed")
    @Expose
    private String speed;

    public String getSpeed() {
        return speed;
    }
}
