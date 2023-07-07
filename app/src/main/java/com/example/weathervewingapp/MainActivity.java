package com.example.weathervewingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.content.pm.PackageManager;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.apache.commons.lang3.text.WordUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    String lang;
    double lon, lat;
    TextView city, temp, description, feelsLike, speedWind, humidity;
    TextView thirdDay, fourthDay, fifthDay;
    TextView tomorrowTemp, thirdTemp, fourthTemp, fifthTemp;
    TextView tomorrowFeelsLike, thirdFeelsLike, fourthFeelsLike, fifthFeelsLike;
    ImageView icon;
    ImageView iconTomorrow, iconThird, iconFourth, iconFifth;
    final String iconURL = "https://openweathermap.org/img/wn/";
    private final int REQUEST_LOCATION_PERMISSION = 1;
    SwipeRefreshLayout swipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);

                getCurrentWeatherResponse();
                getForecastWeather();
            }
        });

        lang = Locale.getDefault().getLanguage();

        city = findViewById(R.id.city);
        description = findViewById(R.id.description);
        temp = findViewById(R.id.temp);
        icon = findViewById(R.id.icon);
        feelsLike = findViewById(R.id.feelsLike);
        speedWind = findViewById(R.id.speedWind);
        humidity = findViewById(R.id.humidity);

        thirdDay = findViewById(R.id.thirdDay);
        fourthDay = findViewById(R.id.fourthDay);
        fifthDay = findViewById(R.id.fifthDay);

        tomorrowTemp = findViewById(R.id.tomorrowTemp);
        thirdTemp = findViewById(R.id.thirdTemp);
        fourthTemp = findViewById(R.id.fourthTemp);
        fifthTemp = findViewById(R.id.fifthTemp);

        tomorrowFeelsLike = findViewById(R.id.tomorrowFeelsLike);
        thirdFeelsLike = findViewById(R.id.thirdFeelsLike);
        fourthFeelsLike = findViewById(R.id.fourthFeelsLike);
        fifthFeelsLike = findViewById(R.id.fifthFeelsLike);

        iconTomorrow = findViewById(R.id.iconTomorrow);
        iconThird = findViewById(R.id.iconThird);
        iconFourth = findViewById(R.id.iconFourth);
        iconFifth = findViewById(R.id.iconFifth);

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        LocationListener locationListener = new MyLocationListener();

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            requestLocationPermission();
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, locationListener);
    }

    private class MyLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            lon = location.getLongitude();
            lat = location.getLatitude();

            getCurrentWeatherResponse();
            getForecastWeather();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NotNull String[] permissions,
                                           @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @AfterPermissionGranted(REQUEST_LOCATION_PERMISSION)
    public void requestLocationPermission() {
        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};
        if (!EasyPermissions.hasPermissions(this, perms)) {
            EasyPermissions.requestPermissions(this, "Please grant the location permission",
                    REQUEST_LOCATION_PERMISSION, perms);
        }
    }

    private void getCurrentWeatherResponse() {
        OpenWeatherRequest openWeatherRequest = new OpenWeatherRequest();

        NetworkService.getInstance()
                .getJSONAPI()
                .getCurrentWeather(openWeatherRequest.getAppid(), lang, lat, lon, openWeatherRequest.getUnits())
                .enqueue(new Callback<CurrentWeatherResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<CurrentWeatherResponse> call,
                                           @NonNull Response<CurrentWeatherResponse> response) {

                        CurrentWeatherResponse currentWeather = response.body();
                        try {
                            loadCurrentWeather(currentWeather);
                        }
                            catch (NullPointerException e){
                            Toast.makeText(getApplicationContext(), "Error connected",
                                    Toast.LENGTH_LONG).show();
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

    private void loadCurrentWeather(@NonNull CurrentWeatherResponse currentWeather){
        city.setText(currentWeather.getName());

        String strDescription = currentWeather.getWeather().get(0).getDescription();
        strDescription = strDescription.substring(0, 1).toUpperCase() + strDescription.substring(1);
        description.setText(strDescription);

        String strTemp = (int)currentWeather.getMain().getTemp() + "°C";
        temp.setText(strTemp);

        String imagePath = currentWeather.getWeather().get(0).getIcon()+".png";
        Picasso.get().load(iconURL + imagePath).into(icon);

        String strFeelsLike =(int)currentWeather.getMain().getFeelsLike() +"°C";
        feelsLike.setText(strFeelsLike);

        String strSpeedWind = currentWeather.getWind().getSpeed();
        speedWind.setText(strSpeedWind);

        String strHumidity = currentWeather.getMain().getHumidity() + "%";
        humidity.setText(strHumidity);

    }

    private void getForecastWeather(){
        OpenWeatherRequest openWeatherRequest = new OpenWeatherRequest();

        NetworkService.getInstance()
                .getJSONAPI()
                .getForecastWeather(openWeatherRequest.getAppid(), lang, lat, lon, openWeatherRequest.getUnits())
                .enqueue(new Callback<ForecastWeatherResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<ForecastWeatherResponse> call,
                                           @NonNull Response<ForecastWeatherResponse> response) {

                        ForecastWeatherResponse forecastWeather = response.body();
                        try {
                            loadForecastWeather(forecastWeather);
                        }
                        catch (NullPointerException e){
                            Toast.makeText(getApplicationContext(), "Error connected",
                                    Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ForecastWeatherResponse> call,
                                          @NonNull Throwable t) {
                        Toast.makeText(getApplicationContext(), "An error has occured",
                                Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void loadForecastWeather(@NonNull ForecastWeatherResponse forecastWeather){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        setDayOfWeek(calendar);

        HandlerForecastWeatherJSON handlerForecastWeatherJSON = new HandlerForecastWeatherJSON();

        int index = handlerForecastWeatherJSON.checkIndex(calendar, forecastWeather);

        ArrayList<Integer> listMaxTemp = handlerForecastWeatherJSON.computeListMaxTemp(index, forecastWeather);
        tomorrowTemp.setText(listMaxTemp.get(0) + "°C");
        thirdTemp.setText(listMaxTemp.get(1) + "°C");
        fourthTemp.setText(listMaxTemp.get(2) + "°C");
        fifthTemp.setText(listMaxTemp.get(3) + "°C");

        ArrayList<Integer> listMinTemp = handlerForecastWeatherJSON.computeListMinTemp(index, forecastWeather);
        tomorrowFeelsLike.setText(listMinTemp.get(0) + "°C");
        thirdFeelsLike.setText(listMinTemp.get(1) + "°C");
        fourthFeelsLike.setText(listMinTemp.get(2) + "°C");
        fifthFeelsLike.setText(listMinTemp.get(3) + "°C");

        ArrayList<String> listIcons = handlerForecastWeatherJSON.computeListIcons(index, forecastWeather);
        Picasso.get().load(iconURL + listIcons.get(0) + ".png").into(iconTomorrow);
        Picasso.get().load(iconURL + listIcons.get(1) + ".png").into(iconThird);
        Picasso.get().load(iconURL + listIcons.get(2) + ".png").into(iconFourth);
        Picasso.get().load(iconURL + listIcons.get(3) + ".png").into(iconFifth);
    }

    private void setDayOfWeek(Calendar calendar){
        SimpleDateFormat dateFormat = new SimpleDateFormat("EE", Locale.getDefault());

        calendar.add(Calendar.DAY_OF_WEEK, 2);
        String strThirdDay = dateFormat.format(calendar.getTime());
        thirdDay.setText(WordUtils.capitalize(strThirdDay));

        calendar.add(Calendar.DAY_OF_WEEK, 1);
        String strFourthDay = dateFormat.format(calendar.getTime());
        fourthDay.setText(WordUtils.capitalize(strFourthDay));

        calendar.add(Calendar.DAY_OF_WEEK, 1);
        String strFifthDay = dateFormat.format(calendar.getTime());
        fifthDay.setText(WordUtils.capitalize(strFifthDay));
    }


}