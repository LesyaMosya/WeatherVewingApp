package com.example.weathervewingapp;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import java.util.Date;

public class WeatherData extends BaseObservable {
    private Date date;
    private String tempCurrent;
    private String tempFeelsLike;
    private String iconName;

    public WeatherData(Date date, String iconName, String tempCurrent, String tempFeelsLike) {
        this.date = date;
        this.iconName = iconName;
        this.tempCurrent = tempCurrent;
        this.tempFeelsLike = tempFeelsLike;
    }
    public WeatherData(String tempCurrent, String tempFeelsLike, String iconName) {
        this.tempCurrent = tempCurrent;
        this.tempFeelsLike = tempFeelsLike;
        this.iconName = iconName;
    }
    @Bindable
    public String getTempCurrent() {
        return tempCurrent;
    }
    public void setTempCurrent(String tempCurrent) {
        this.tempCurrent = tempCurrent;
        notifyPropertyChanged(BR.tempCurrent);
    }
    @Bindable
    public String getTempFeelsLike() {
        return tempFeelsLike;
    }
    public void setTempFeelsLike(String tempFeelsLike) {
        this.tempFeelsLike = tempFeelsLike;
        notifyPropertyChanged(BR.tempFeelsLike);
    }

    @Bindable
    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
        notifyPropertyChanged(BR.date);
    }

    @Bindable
    public String getIconName() {
        return iconName;
    }
    public void setIconName(String iconName) {
        this.iconName = iconName;
        notifyPropertyChanged(BR.iconName);
    }
}
