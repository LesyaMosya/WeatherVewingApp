package com.example.weathervewingapp;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OpenWeatherAPI {
    @GET("/weather")
    Call<CurrentWeatherResponse> getData(
            @Body OpenWeatherRequest data
    );
}
