package com.example.weathervewingapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class WeatherDataAdapter extends RecyclerView.Adapter<WeatherDataAdapter.ViewHolder>{
    private final LayoutInflater inflater;
    private final List<WeatherData> listWeatherData;
    private final Context context;

    WeatherDataAdapter(Context context, List<WeatherData> listWeatherData) {
        this.listWeatherData = listWeatherData;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }
    @NonNull
    @Override
    public WeatherDataAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.list_forecast_weather, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(WeatherDataAdapter.ViewHolder holder, int position) {
        WeatherData weatherData = listWeatherData.get(position);
        holder.dayView.setText(Converter.convertDate(weatherData.getDate(), context));
        Converter.convertCodeIcon(holder.iconView, weatherData.getIconName());
        holder.maxTempView.setText(weatherData.getTempCurrent() + "°C");
        holder.feelsLikeTempView.setText(weatherData.getTempFeelsLike() + "°C");


    }

    @Override
    public int getItemCount() {
        return listWeatherData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView iconView;
        final TextView dayView, maxTempView, feelsLikeTempView;
        ViewHolder(View view){
            super(view);
            dayView = view.findViewById(R.id.day_of_week);
            iconView = view.findViewById(R.id.icon_weather);
            maxTempView = view.findViewById(R.id.max_temp);
            feelsLikeTempView = view.findViewById(R.id.feels_life_temp);
        }
    }
}
