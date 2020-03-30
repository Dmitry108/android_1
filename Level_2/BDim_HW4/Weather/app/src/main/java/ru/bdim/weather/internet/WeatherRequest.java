package ru.bdim.weather.internet;

public class WeatherRequest {
    private Coord coord;
    private Weather[] weather;
    private String base;
    private Main main;
    private int visibility;
    private Wind wind;
    private Clouds clouds;
    private int dt;
    private Sys sys;
    private int timezone;
    private int id;
    private String name;
    private int cod;

    public String getName() {
        return name;
    }

    public int getTemperature(){
        return Math.round(getMain().getTemperature());
    }
    public Weather getWeather(){ return weather[0]; }
    public Main getMain(){
        return main;
    }
    public Wind getWind() { return wind; }
}