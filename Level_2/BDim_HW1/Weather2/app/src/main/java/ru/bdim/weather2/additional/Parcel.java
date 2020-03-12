package ru.bdim.weather2.additional;

import java.io.Serializable;

public class Parcel implements Serializable {
    private Weather weather;
    private boolean isExtra;
    private boolean isSunAndMoon;

    public Parcel(String city, String date){//}, boolean isExtra, boolean isSunAndMoon) {
        this.weather = new Weather(city, date);
        this.isExtra = false;//isExtra;
        this.isSunAndMoon = false;//isSunAndMoon;
    }
    public Parcel(Weather weather){
        this.weather = weather;
        this.isExtra = false;
        this.isSunAndMoon = false;
    }

    public String getCity() {
        return weather.getCity();
    }
    public void setCity(String city, String date){
        weather.refresh(city, date);
    }

    public Weather getWeather() { return weather; }

    public void setWeather(Weather weather) { this.weather = weather; }

    public boolean isExtra() {
        return isExtra;
    }

    public void setParameters(boolean extra, boolean sunMoon) {
        this.isExtra = extra;
        this.isSunAndMoon = sunMoon;
    }
}