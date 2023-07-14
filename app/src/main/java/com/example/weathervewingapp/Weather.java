package com.example.weathervewingapp;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import java.util.List;

public class Weather extends BaseObservable {
    private String city;
    private String description;
    private WeatherData currentTemp;
    private List<WeatherData> forecastTemps;
    private String windSpeed;
    private String humidity;

    public Weather(List<WeatherData> forecastTemps){
        this.forecastTemps = forecastTemps;
    }
    public Weather(String city, String description, WeatherData currentTemp, String windSpeed,
                   String humidity) {
        this.city = city;
        this.description = description;
        this.currentTemp = currentTemp;
        this.windSpeed = windSpeed;
        this.humidity = humidity;
    }

    @Bindable
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
        notifyPropertyChanged(BR.city);
    }

    @Bindable
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
        notifyPropertyChanged(BR.description);
    }

    @Bindable
    public WeatherData getCurrentTemp() {
        return currentTemp;
    }
    public void setCurrentTemp(WeatherData currentTemp) {
        this.currentTemp = currentTemp;
        notifyPropertyChanged(BR.currentTemp);
    }

    @Bindable
    public List<WeatherData> getForecastTemps() {
        return forecastTemps;
    }
    public void setForecastTemps(List<WeatherData> forecastTemps) {
        this.forecastTemps = forecastTemps;
        notifyPropertyChanged(BR.forecastTemps);
    }

    @Bindable
    public String getWindSpeed() {
        return windSpeed;
    }
    public void setWindSpeed(String windSpeed) {
        this.windSpeed = windSpeed;
        notifyPropertyChanged(BR.windSpeed);
    }

    @Bindable
    public String getHumidity() {
        return humidity;
    }
    public void setHumidity(String humidity) {
        this.humidity = humidity;
        notifyPropertyChanged(BR.humidity);
    }
}
