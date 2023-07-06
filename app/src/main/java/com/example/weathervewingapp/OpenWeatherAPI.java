package com.example.weathervewingapp;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OpenWeatherAPI {
    @GET("weather")
    Call<CurrentWeatherResponse> getCurrentWeather(
            @Query("appid") String appid,
            @Query("lang") String lang,
            @Query("lat") double lat,
            @Query("lon") double lon,
            @Query("units") String units
    );

    @GET("forecast")
    Call<ForecastWeatherResponse> getForecastWeather(
            @Query("appid") String appid,
            @Query("lang") String lang,
            @Query("lat") double lat,
            @Query("lon") double lon,
            @Query("units") String units
    );
}
