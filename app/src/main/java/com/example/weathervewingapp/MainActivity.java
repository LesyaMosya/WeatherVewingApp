package com.example.weathervewingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        NetworkService.getInstance()
                .getJSONApi()
                .getData()
                .enqueue(new Callback<CurrentWeatherResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<CurrentWeatherResponse> call,
                                           @NonNull Response<CurrentWeatherResponse> response)
                    {
                        CurrentWeatherResponse currentWeather = response.body();

                        textView.append(currentWeather.getMain() + "\n");
                        textView.append(currentWeather.getWeather() + "\n");
                        textView.append(currentWeather.getWind() + "\n");
                        textView.append(currentWeather.getName() + "\n");
                    }

                    @Override
                    public void onFailure(@NonNull Call<CurrentWeatherResponse> call,
                                          @NonNull Throwable t) {

                        textView.append("Error occurred while getting request!");
                        t.printStackTrace();
                    }
                });

        SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.refreshLayout);
        swipeRefreshLayout.setOnRefreshListener(
                () -> swipeRefreshLayout.setRefreshing(false)
        );
    }

}