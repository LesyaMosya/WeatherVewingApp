package com.example.weathervewingapp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WindJSON {

    @SerializedName("speed")
    @Expose
    private int speed;

    public int getSpeed() {
        return speed;
    }
    public void setSpeed(int speed) {
        this.speed = speed;
    }
}
