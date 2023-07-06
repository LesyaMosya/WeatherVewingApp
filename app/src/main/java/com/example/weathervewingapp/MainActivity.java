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
    ImageView icon;
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

                getWeatherResponse();
            }
        });

        city = findViewById(R.id.city);
        description = findViewById(R.id.description);
        temp = findViewById(R.id.temp);
        icon = findViewById(R.id.icon);
        feelsLike = findViewById(R.id.feelsLike);
        speedWind = findViewById(R.id.speedWind);
        humidity = findViewById(R.id.humidity);

        thirdDay = findViewById(R.id.thirdDay);
        fourthDay = findViewById(R.id.fourthDay);
        fifthDay = findViewById(R.id.fifthDay);git

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
    private void getWeatherResponse() {
        OpenWeatherRequest openWeatherRequest = new OpenWeatherRequest();

        lang = Locale.getDefault().getLanguage();

        NetworkService.getInstance()
                .getJSONAPI()
                .getCurrentWeather(openWeatherRequest.getAppid(), lang, lat, lon, openWeatherRequest.getUnits())
                .enqueue(new Callback<CurrentWeatherResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<CurrentWeatherResponse> call,
                                           @NonNull Response<CurrentWeatherResponse> response) {
                        setDayOfWeek();

                        CurrentWeatherResponse currentWeather = response.body();
                        loadCurrentWeather(currentWeather);
                    }

                    @Override
                    public void onFailure(@NonNull Call<CurrentWeatherResponse> call,
                                          @NonNull Throwable t) {
                        Toast.makeText(getApplicationContext(), "An error has occured",
                                Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void loadCurrentWeather(CurrentWeatherResponse currentWeather){
        if (currentWeather != null) {

            city.setText(currentWeather.getName());

            String strDescription = currentWeather.getWeather().get(0).getDescription();
            description.setText(WordUtils.capitalize(strDescription));

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
    }

    private class MyLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            lon = location.getLongitude();
            lat = location.getLatitude();

            getWeatherResponse();
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


    private void setDayOfWeek(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("EE");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

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