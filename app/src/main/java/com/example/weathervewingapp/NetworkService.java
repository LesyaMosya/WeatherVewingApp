package com.example.weathervewingapp;
import androidx.annotation.NonNull;

import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkService {
    private static NetworkService mInstance = null;
    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5/";
    private static Retrofit mRetrofit;

    private NetworkService() {
        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static NetworkService getInstance() {
        if (mInstance == null) {
            mInstance = new NetworkService();
        }
        return mInstance;
    }

    public OpenWeatherAPI getJSONAPI(){
        return mRetrofit.create(OpenWeatherAPI.class);
    }


}
