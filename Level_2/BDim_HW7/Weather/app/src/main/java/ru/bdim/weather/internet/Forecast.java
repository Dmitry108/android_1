package ru.bdim.weather.internet;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

import ru.bdim.weather.internet.Weather;

public class Forecast implements Convertable {
    @SerializedName("dt")
    private long dt; //1586077200
    @SerializedName("main")
    private Main main;
    @SerializedName("weather")
    private Weather[] weathers;
    @SerializedName("clouds")
    private Clouds clouds;
    @SerializedName("wind")
    private Wind wind;
    @SerializedName("sys")
    private Sys sys;
    @SerializedName("dt_txt")
    private String dt_txt;

    public long getDate (){
        return dt;
    }

    public Main getMain() {
        return main;
    }

    public int getTemperature() {
        return Math.round(getMain().getTemperature());
    }
    public String getSky() {
        return weathers[0].getIcon();
    }
}