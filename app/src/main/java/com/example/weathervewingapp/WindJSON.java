package com.example.weathervewingapp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WindJSON {

    @SerializedName("speed")
    @Expose
    private float speed;

    public float getSpeed() {
        return speed;
    }
}
