package com.example.weathervewingapp;

import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.squareup.picasso.Picasso;

import org.apache.commons.lang3.StringUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Converter {

    static final String iconURL = "https://openweathermap.org/img/wn/";

    @BindingAdapter({"app:url"})
    public static void convertCodeIcon(ImageView icon, String iconCode) {
        Picasso.get().load(iconURL + iconCode + ".png").into(icon);
    }

    public static String convertDate(Date date, Context context){
        SimpleDateFormat dateFormat = new SimpleDateFormat("EE", Locale.getDefault());

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, 1);
        String tomorrow = dateFormat.format(calendar.getTime());

        String formatDate = dateFormat.format(date);

        if(tomorrow.equals(formatDate)){
            return context.getString(R.string.tomorrow);
        }
        else {
            return StringUtils.capitalize(formatDate);
        }
    }
}
