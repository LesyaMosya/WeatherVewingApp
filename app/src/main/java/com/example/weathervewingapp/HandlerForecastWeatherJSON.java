package com.example.weathervewingapp;

import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;

import androidx.annotation.NonNull;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class HandlerForecastWeatherJSON {
    public int checkIndex(Calendar calendar, @NonNull ForecastWeatherResponse forecastWeather){
        calendar.add(Calendar.DAY_OF_WEEK, -3);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        String tomorrow = dateFormat.format(calendar.getTime());

        int step;
        String strDateOfStep;

        for (step=0; step<40; step++) {
            strDateOfStep = forecastWeather.getList().get(step).getDt_txt();
            try {
                Date currentDate = dateFormat.parse(strDateOfStep);
                strDateOfStep = dateFormat.format(currentDate);
            }
            catch (ParseException e){
                e.printStackTrace();
            }
            if (tomorrow.equals(strDateOfStep)) {
                break;
            }
        }

        return step;
    }

    public ArrayList<Integer> computeListMaxTemp(int index, @NonNull ForecastWeatherResponse forecastWeather){
        ArrayList<Integer> arrayMaxTemp = new ArrayList<>();

        int step;
        int countOfGaps = 0;

        float stepMaxTemp;
        float maxTemp = -40;

        for (step = index; step < 40;) {
            while (countOfGaps < 8) {
                stepMaxTemp = forecastWeather.getList().get(step).getMain().getTemp_max();
                maxTemp = Math.max(maxTemp, stepMaxTemp);

                    countOfGaps++;
                    step++;
            }
            arrayMaxTemp.add((int)maxTemp);

            if ( arrayMaxTemp.size() == 4)
            {
                break;
            }
            else {
                maxTemp = 0;
                countOfGaps = 0;
            }
        }

        return arrayMaxTemp;
    }

    public ArrayList<Integer> computeListMinTemp(int index, @NonNull ForecastWeatherResponse forecastWeather){
        ArrayList<Integer> arrayMinTemp = new ArrayList<>();

        int step;
        int countOfGaps = 0;

        float stepMinTemp;
        float sumMinTemp = 0;

        int count = 0;

        for (step = index; step < 40;) {
            while (countOfGaps < 8) {
                stepMinTemp = forecastWeather.getList().get(step).getMain().getFeelsLike();
                sumMinTemp += stepMinTemp;

                    count++;
                    countOfGaps++;
                    step++;
            }
            arrayMinTemp.add((int)(sumMinTemp/count));

            if ( arrayMinTemp.size() == 4)
            {
                break;
            }
            else {
                sumMinTemp = 0;
                count = 0;
                countOfGaps = 0;
            }
        }

        return arrayMinTemp;
    }

    public ArrayList<String> computeListIcons(int index, @NonNull ForecastWeatherResponse forecastWeather){
        ArrayList<String> listIcons = new ArrayList<>();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        String timeForIcon = "12:00:00";

        int step;
        String strDateOfStep = null;

        String iconCode;

        for (step=index; step < 40; ) {
            strDateOfStep = forecastWeather.getList().get(step).getDt_txt();
            try {
                Date DateOfStep = dateFormat.parse(strDateOfStep);
                strDateOfStep = timeFormat.format(DateOfStep);
            }
            catch (ParseException e){
                e.printStackTrace();
            }
            if (timeForIcon.equals(strDateOfStep)) {
                iconCode = forecastWeather.getList().get(step).getWeather().get(0).getIcon();
                listIcons.add(iconCode);
                step += 7;
            }
            step++;
        }
        return listIcons;
    }
}
