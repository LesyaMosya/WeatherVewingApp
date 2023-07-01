package com.example.weathervewingapp;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface OpenWeatherAPI {
    @GET("/posts/{id}")
    public Call<Post> getWeather(
            @Path("id") String id,
            @Query("lat") float lat,
            @Query("lon") float lon,
            @Query("appid") String appid,
            @Query("lang") String lang,
            @Query("units") String units
    );
}
