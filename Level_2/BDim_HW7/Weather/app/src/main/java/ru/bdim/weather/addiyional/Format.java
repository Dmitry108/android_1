package ru.bdim.weather.addiyional;

import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.Log;
import android.view.View;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import ru.bdim.weather.R;

public class Format implements Constants {

    public static String getTempC(int t){
        return String.format(Locale.ROOT, "%s%d \u00B0C", t > 0 ? "+" : "", t);
    }
    public static int getImgSky(View view, int sky){
        TypedArray imageArray = view.getResources().obtainTypedArray(R.array.img_sky);
        int image = imageArray.getResourceId(sky, -1);
        imageArray.recycle();
        return image;
    }
    public static int getDirFromDegree(int deg, int count){
        int dir = ((deg + count/2)%360)*count/360;
        Log.d(TAG, String.format("%d = %d/16 = %d", deg, (deg + count/2)%360, dir));
        return dir;
    }
    public static int getIconNumber(String icon) {
        int number;
        switch (icon.substring(0, 2)) {
            case "01": number = 0; break;
            case "02": number = 1; break;
            case "03":
            case "04": number = 2; break;
            case "09": number = 3; break;
            case "10": number = 4; break;
            case "11": number = 5; break;
            case "13": number = 6; break;
            case "50":
            default: number = 7;
        }
        return number;
    }
    public static String dateToString(Date date){
        SimpleDateFormat dateFormat = new SimpleDateFormat("d MMMM yyyy", Locale.getDefault());
        return dateFormat.format(date);
    }
    public static String timeToString(Date date){
        SimpleDateFormat dateFormat = new SimpleDateFormat("H:mm", Locale.getDefault());
        return dateFormat.format(date);
    }
    public static Date stringToDate(String date){
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("d MMMM yyyy", Locale.getDefault());
            return dateFormat.parse(date);
        } catch (ParseException e) {
            Log.e(TAG, e.getMessage());
        }
        return null;
    }
    // метод определяет цвет в спектре от синего до красного
    public static int getRGB (int x, int t0, int t){
        int r, g, b;
        int dt = (t - t0);
        if (x < t0){
            r = g = 0; b = 255;
        } else if (x < t0 + dt / 4) {
            r = 0; g = 1024*(x - t0)/dt; b = 255;
        } else if (x < t0 + dt/2) {
            r = 0; g = 255; b = -1024/dt*(x - t0 - dt/2);
        } else if (x < t0 + 3*dt/4) {
            r = 1024*(x - t0 - dt/2)/dt; g = 255; b = 0;
        } else if (x < t) {
            r = 255; g =-1024/dt*(x - t); b = 0;
        } else {
            r = 255; g = b = 0;
        }
        return Color.rgb(r, g, b);
    }
}