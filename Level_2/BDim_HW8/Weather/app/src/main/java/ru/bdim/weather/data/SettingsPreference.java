package ru.bdim.weather.data;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

public class SettingsPreference {
    private SharedPreferences preference;

    private final static String FILE_PREF = "file_pref";
    private final static String CITY_PREF = "city_pref";
    private final static String EXTRA_PREF = "extra_pref";
    private final static String HOURLY_PREF = "hourly_pref";
    private final static String LOCAL_PREF = "location_pref";

    public SettingsPreference(@NonNull Context context){
        preference = context.getSharedPreferences(FILE_PREF, Context.MODE_PRIVATE);
    }
    public String getSavedCity() {
        return preference.getString(CITY_PREF, "");
    }
    public boolean getSavedExtra() {
        return preference.getBoolean(EXTRA_PREF, true);
    }
    public boolean getSavedHourly() {
        return preference.getBoolean(HOURLY_PREF, true);
    }
    public boolean getSavedLocation() {
        return preference.getBoolean(LOCAL_PREF, true);
    }
    public void saveCity(String city) {
        if (city == null) { city = ""; }
        SharedPreferences.Editor editor = preference.edit();
        editor.putString(CITY_PREF, city);
        editor.apply();
    }
    public void saveSettings(boolean isExtra, boolean isHourly) {
        SharedPreferences.Editor editor = preference.edit();
        editor.putBoolean(EXTRA_PREF, isExtra);
        editor.putBoolean(HOURLY_PREF, isHourly);
        editor.apply();
    }
    public void saveLocation(boolean isLocation){
        SharedPreferences.Editor editor = preference.edit();
        editor.putBoolean(LOCAL_PREF, isLocation);
        editor.apply();
    }
}