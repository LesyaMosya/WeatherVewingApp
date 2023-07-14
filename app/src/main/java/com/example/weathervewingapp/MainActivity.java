package com.example.weathervewingapp;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.weathervewingapp.databinding.ActivityWeatherBinding;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Locale;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    String lang;
    double lon, lat;

    private final int REQUEST_LOCATION_PERMISSION = 1;
    SwipeRefreshLayout swipeRefreshLayout;
    ActivityWeatherBinding binding;
    RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_weather);

        recyclerView = findViewById(R.id.list);
        lang = Locale.getDefault().getLanguage();

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            swipeRefreshLayout.setRefreshing(false);
            getCurrentWeatherResponse();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getCurrentWeatherResponse();
    }

    private class MyLocationListener implements LocationListener {

        public void setUpLocationListener(Context context)
        {
            LocationManager locationManager = (LocationManager)
                    context.getSystemService(Context.LOCATION_SERVICE);

            LocationListener locationListener = new MyLocationListener();


            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, locationListener);

            lat = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLatitude();
            lon = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLongitude();
        }

        @Override
        public void onLocationChanged(Location location) {
/*            lon = location.getLongitude();
            lat = location.getLatitude();

            getCurrentWeatherResponse();*/
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NotNull String[] permissions,
                                           @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);

        if (EasyPermissions.hasPermissions(this, permissions)) {
           getCurrentWeatherResponse();
        }
    }

    @AfterPermissionGranted(REQUEST_LOCATION_PERMISSION)
    public void requestLocationPermission() {
        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if (!EasyPermissions.hasPermissions(this, perms)) {

            EasyPermissions.requestPermissions(this,
                    getResources().getString(R.string.permission), REQUEST_LOCATION_PERMISSION, perms);
        }
    }
    private void getCurrentWeatherResponse() {

        LocationManager locationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestLocationPermission();
            return;
        }

        lat = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLatitude();
        lon = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLongitude();

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
                            getForecastWeatherResponse();
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