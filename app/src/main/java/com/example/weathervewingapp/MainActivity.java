package com.example.weathervewingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    TextView city;
    TextView temp;
    TextView description;
    ImageView icon;
    TextView feelsLike;
    TextView speedWind;
    TextView humidity;
    final String iconURL = "https://openweathermap.org/img/wn/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        city = findViewById(R.id.city);
        description = findViewById(R.id.description);
        temp = findViewById(R.id.temp);
        icon = findViewById(R.id.icon);
        feelsLike = findViewById(R.id.feelsLike);
        speedWind = findViewById(R.id.speedWind);
        humidity = findViewById(R.id.humidity);

        getCurrentWeather();
    }
    private void getCurrentWeather() {
        OpenWeatherRequest openWeatherRequest = new OpenWeatherRequest();

        openWeatherRequest.setLang(Locale.getDefault().getLanguage()); // узнаем какой язык

        NetworkService.getInstance()
                .getJSONAPI()
                .getWeather("32d6218651879bf8d4e4e888b784551f", openWeatherRequest.getLang(),
                        59.9386, 30.3141, "metric")
                .enqueue(new Callback<CurrentWeatherResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<CurrentWeatherResponse> call,
                                           @NonNull Response<CurrentWeatherResponse> response) {
                        CurrentWeatherResponse currentWeather = response.body();

                        if (currentWeather != null) {

                            String strCity = currentWeather.getName();
                            city.setText(strCity.toCharArray(),0,strCity.length());

                            String strDescription=currentWeather.getWeather().get(0).getDescription();
                            description.setText(strDescription.toCharArray(),0,strDescription.length());

                            String strTemp = (int)currentWeather.getMain().getTemp() + "°C";
                            temp.setText(strTemp.toCharArray(), 0, strTemp.length());

                            String imagePath = currentWeather.getWeather().get(0).getIcon()+".png";
                            loadIcon(iconURL + imagePath);

                            String strFeelsLike =(int)currentWeather.getMain().getFeelsLike() +"°C";
                            feelsLike.setText(strFeelsLike.toCharArray(),0, strFeelsLike.length());

                            String strSpeedWind = currentWeather.getWind().getSpeed();
                            speedWind.setText(strSpeedWind.toCharArray(),0, strSpeedWind.length());

                            String strHumidity = currentWeather.getMain().getHumidity() + "%";
                            humidity.setText(strHumidity.toCharArray(),0, strHumidity.length());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<CurrentWeatherResponse> call,
                                          @NonNull Throwable t) {
                        Toast.makeText(getApplicationContext(), "An error has occured",
                                Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void loadIcon(String url){
        Picasso.get().load(url).into(icon);
    }
}