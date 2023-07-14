package com.example.weathervewingapp;

import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;

import androidx.annotation.NonNull;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HandlerForecastWeatherJSON {

    private Date convertCurrentData(String currentDate){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date convertedDate = null;
        try {
            convertedDate = format.parse(currentDate);
        }
        catch (ParseException e){
            e.printStackTrace();
        }

        return convertedDate;
    }
    public List<WeatherData> setListForecastWeather(ForecastWeatherResponse forecastWeather){
        List<WeatherData> listForecastWeather = new ArrayList<>();

        Date date;
        String iconCode = null;
        int maxTemp = -40;
        int avgTemp;

        float sumTemp = 0;
        float stepFeelsLikeTemp;
        int count = 0;

        int countOfGaps = 0;
        String currentDate = null;

        int step = searchIndex(forecastWeather);

        while (step < 40 && listForecastWeather.size()<4){
            while (countOfGaps < 8) {
                currentDate = forecastWeather.getList().get(step).getDt_txt();
                String checkedIcon = getIconCode(forecastWeather, currentDate, step);
                if (checkedIcon != null) {
                    iconCode = checkedIcon;
                }

                maxTemp = Math.max(maxTemp, (int) forecastWeather.getList().get(step).getMain().getFeelsLike());

                stepFeelsLikeTemp = forecastWeather.getList().get(step).getMain().getFeelsLike();
                sumTemp += stepFeelsLikeTemp;

                count++;
                countOfGaps ++;
                step++;
            }
            countOfGaps = 0;

            avgTemp = (int)sumTemp/count;
            date = convertCurrentData(currentDate);

            listForecastWeather.add(new WeatherData(date, iconCode, String.valueOf(maxTemp), String.valueOf(avgTemp)));
        }

        return listForecastWeather;
    }

    private int searchIndex(@NonNull ForecastWeatherResponse forecastWeather){

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        String today = format.format(calendar.getTime());

        Date currentDate;
        boolean flag = false;
        int index = 0;

        while (!flag){
            String strCurrentDate = forecastWeather.getList().get(index).getDt_txt();
            currentDate = convertCurrentData(strCurrentDate);
            strCurrentDate = format.format(currentDate);
            if (!today.equals(strCurrentDate)){
                flag = true;
            }
            else {
                index++;
            }
        }
        return index;
    }

    private String getIconCode(@NonNull ForecastWeatherResponse forecastWeather, String currentDate, int index){

        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        String timeForIcon = "12:00:00";

        String strDateOfStep;
        String iconCode = null;

        Date DateOfStep = convertCurrentData(currentDate);
        strDateOfStep = timeFormat.format(DateOfStep);

        if (timeForIcon.equals(strDateOfStep)) {
            iconCode = forecastWeather.getList().get(index).getWeather().get(0).getIcon();
        }
        return iconCode;
    }
}
