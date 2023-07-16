package com.example.weathervewingapp;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.weathervewingapp.databinding.ActivityWeatherBinding;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Locale;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    boolean flagBans = false;
    String lang;
    double lon, lat;

    private static final String[] PERMISSIONS_LOCATION = {Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION};
    private static final int RC_LOCATION = 126;

    SwipeRefreshLayout swipeRefreshLayout;
    ActivityWeatherBinding binding;
    RecyclerView recyclerView;


    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_weather);

        recyclerView = findViewById(R.id.list);
        lang = Locale.getDefault().getLanguage();

        Toolbar mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setTitleTextColor(getResources().getColor(R.color.title, getTheme()));
        mToolbar.setLogo(R.mipmap.ic_weather_foreground);

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            swipeRefreshLayout.setRefreshing(false);
            getLocation();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getLocation();
    }

    private class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            lon = location.getLongitude();
            lat = location.getLatitude();
        }
    }

    private void getLocation(){
        LocationManager locationManager = (LocationManager)this.getSystemService(LOCATION_SERVICE);
        LocationListener locationListener = new MyLocationListener();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestLocationPermission();
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                1000 * 10, 10, locationListener);

        lat = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLatitude();
        lon = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLongitude();

        getCurrentWeatherResponse();
        getForecastWeatherResponse();
    }

    @AfterPermissionGranted(RC_LOCATION)
    public void requestLocationPermission() {
        if (hasLocationPermission()){
            Toast.makeText(this, getResources().getString(R.string.success), Toast.LENGTH_SHORT)
                    .show();
        }
        else {
            EasyPermissions.requestPermissions(this, getResources().getString(R.string.permission),
                    RC_LOCATION, PERMISSIONS_LOCATION);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NotNull String[] permissions,
                                           @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    private boolean hasLocationPermission() {
        return EasyPermissions.hasPermissions(this, PERMISSIONS_LOCATION);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        Log.d(TAG, "onPermissionsGranted:" + requestCode + ":" + perms.size());
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        Log.d(TAG, "onPermissionsDenied:" + requestCode + ":" + perms.size());
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            if (!flagBans) {
                flagBans = true;
                new AppSettingsDialog.Builder(this)
                        .setRationale(getResources().getString(R.string.description_error))
                        .setTitle(getResources().getString(R.string.title_error))
                        .build()
                        .show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            if (!hasLocationPermission()) {
                Toast.makeText(this,
                                getResources().getString(R.string.title_error),
                                Toast.LENGTH_LONG)
                        .show();
            }
            else {
                getLocation();
            }
        }
    }
    private void getCurrentWeatherResponse() {

        OpenWeatherRequest openWeatherRequest = new OpenWeatherRequest();

        NetworkService.getInstance()
                .getJSONAPI()
                .getCurrentWeather(openWeatherRequest.getAppid(), lang, lat, lon, openWeatherRequest.getUnits())
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(@NonNull Call<CurrentWeatherResponse> call,
                                           @NonNull Response<CurrentWeatherResponse> response) {

                        CurrentWeatherResponse currentWeather = response.body();
                        if (currentWeather != null){
                            loadCurrentWeather(currentWeather);
                        }
                        else {
                            Toast.makeText(getApplicationContext(),
                                    getResources().getString(R.string.error), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<CurrentWeatherResponse> call,
                                          @NonNull Throwable t) {
                        Toast.makeText(getApplicationContext(),
                                getResources().getString(R.string.error), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void loadCurrentWeather(@NonNull CurrentWeatherResponse currentWeather){

        String city = currentWeather.getName();

        String description = currentWeather.getWeather().get(0).getDescription();
        description = description.substring(0, 1).toUpperCase() + description.substring(1);

        String currentTemp = (int)currentWeather.getMain().getTemp() + "°C";
        String feelsLike =(int)currentWeather.getMain().getFeelsLike() +"°C";
        String icon = currentWeather.getWeather().get(0).getIcon();
        String speedWind = (int)currentWeather.getWind().getSpeed() + " " + getResources().getString(R.string.speedString);
        String humidity = currentWeather.getMain().getHumidity() + "%";

        binding.setWeather(new Weather(city, description,
                new WeatherData(currentTemp, feelsLike, icon), speedWind, humidity));
    }

    private void getForecastWeatherResponse(){
        OpenWeatherRequest openWeatherRequest = new OpenWeatherRequest();

        NetworkService.getInstance()
                .getJSONAPI()
                .getForecastWeather(openWeatherRequest.getAppid(), lang, lat, lon, openWeatherRequest.getUnits())
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(@NonNull Call<ForecastWeatherResponse> call,
                                           @NonNull Response<ForecastWeatherResponse> response) {

                        ForecastWeatherResponse forecastWeather = response.body();
                        if (forecastWeather != null) {
                            loadForecastWeather(forecastWeather);
                        }
                        else {
                            Toast.makeText(getApplicationContext(),
                                    getResources().getString(R.string.error), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ForecastWeatherResponse> call,
                                          @NonNull Throwable t) {
                        Toast.makeText(getApplicationContext(),
                                getResources().getString(R.string.error), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void loadForecastWeather(@NonNull ForecastWeatherResponse forecastWeather){

        HandlerForecastWeatherJSON handlerForecastWeatherJSON = new HandlerForecastWeatherJSON();
        List<WeatherData> listForecastWeather = handlerForecastWeatherJSON.setListForecastWeather(forecastWeather);

        WeatherDataAdapter adapter = new WeatherDataAdapter(this, listForecastWeather);
        recyclerView.setAdapter(adapter);
    }

}