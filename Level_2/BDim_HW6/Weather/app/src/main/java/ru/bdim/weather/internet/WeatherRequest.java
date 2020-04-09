package ru.bdim.weather.internet;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

import ru.bdim.weather.addiyional.Format;

public class WeatherRequest implements Convertable{//extends Request {
    @SerializedName("coord")
    private Coord coord;
    @SerializedName("weather")
    private Weather[] weathers;
    @SerializedName("base")
    private String base;
    @SerializedName("main")
    private Main main;
    @SerializedName("visibility")
    private int visibility;
    @SerializedName("wind")
    private Wind wind;
    @SerializedName("clouds")
    private Clouds clouds;
    @SerializedName("dt")
    private int dt;
    @SerializedName("sys")
    private Sys sys;
    @SerializedName("timezone")
    private int timezone;
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("cod")
    private int cod;


    public Weather getWeather(){ return weathers[0]; }
    public Main getMain(){
        return main;
    }
    public Wind getWind() { return wind; }

    public String getCity() {
        return name;
    }
    public String getDate(){
        return Format.dateToString(new Date(dt*1000L));
    }
    public int getTemperature(){
        return Math.round(getMain().getTemperature());
    }
    public String getSky() {
        return getWeather().getIcon();
    }

    public String getSkyDescription() {
        return getWeather().getDescription();
    }

    public float getWindSpeed() {
        return getWind().getSpeed();
    }

    public int getWindDir() {
        return getWind().getDeg();
    }

    public int getHumidity() {
        return getMain().getHumidity();
    }

    public int getPressure() {
        return getMain().getPressure();
    }
    public String getExtrim(){
        return getWeather().getMain();
    }
}