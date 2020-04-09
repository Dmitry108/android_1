package ru.bdim.weather.data;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

public class SettingsPreference {
    private SharedPreferences preference;

    private final static String FILE_PREF = "file_pref";
    private final static String CITY_PREF = "city_pref";
    //private final static String EXTRA_PREF = "extra_pref";
    //private final static String SUN_PREF = "sun_pref";

    public SettingsPreference(@NonNull Context context){
        preference = context.getSharedPreferences(FILE_PREF, Context.MODE_PRIVATE);
    }
    public String getSavedCity() {
        return preference.getString(CITY_PREF, "");
    }
    //public boolean getSavedExtra() {
    //    return preference.getBoolean(EXTRA_PREF, false);
    //}
    //public boolean getSavedSun() {
    //    return preference.getBoolean(SUN_PREF, false);
    //}
    public void saveStatus(String city){//, boolean isExtra, boolean isSun) {
        if (city == null) { city = ""; }
        SharedPreferences.Editor editor = preference.edit();
        editor.putString(CITY_PREF, city);
        //editor.putBoolean(EXTRA_PREF, isExtra);
        //editor.putBoolean(SUN_PREF, isSun);
        editor.apply();
    }
}