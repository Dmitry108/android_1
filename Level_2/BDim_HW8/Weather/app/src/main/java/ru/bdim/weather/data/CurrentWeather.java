package ru.bdim.weather.data;

import ru.bdim.weather.addiyional.Format;
import ru.bdim.weather.databases.HistoryEntity;
import ru.bdim.weather.internet.WeatherRequest;

public class CurrentWeather implements Data {
    //Класс-посредник для activity, получает данные от основного класса-координатора Weather
    //преобразует в вид, пригодный для активити
    private String city;
    private String date;
    private int temperature;
    private int sky;
    private String skyDescription;
    private float windSpeed;
    private int windDir;
    private int humidity;
    private int pressure;
    private double lat;
    private double lon;

    public CurrentWeather(){}
    public CurrentWeather(HistoryEntity history){
        setDatabaseData(history);
    }
    public CurrentWeather(WeatherRequest request) {
        setHttpsData(request);
    }

    public void setHttpsData(WeatherRequest request) {
        this.city = request.getCity();
        this.date = request.getDate();
        this.temperature = request.getTemperature();
        this.sky = Format.getIconNumber(request.getSky());
        this.skyDescription = request.getSkyDescription();
        this.windSpeed = request.getWindSpeed();
        this.windDir = Format.getDirFromDegree(request.getWindDir(), 8);
        this.humidity = request.getHumidity();
        this.pressure = request.getPressure();
        this.lat = request.getLat();
        this.lon = request.getLon();
    }
    public void setDatabaseData(HistoryEntity history) {
        this.city = history.getCity();
        this.date = history.getDate();
        this.temperature = history.getTemperature();
        this.sky = history.getSky();
        this.skyDescription = history.getSkyDescription();
        this.windSpeed = history.getWindSpeed();
        this.windDir = history.getWindDir();
        this.humidity = history.getHumidity();
        this.pressure = history.getPressure();
//        this.lat = history.getLat();
//        this.lon = history.getLon();
    }
    public String getDate() {
        return date;
    }
    public String getCity() {
        return city;
    }
    public int getTemperature() {
        return temperature;
    }
    public String getSkyDescription() {
        return skyDescription;
    }
    public int getSky() {
        return sky;
    }
    public float getWindSpeed() {
        return windSpeed;
    }
    public int getWindDir() {
        return windDir;
    }
    public int getHumidity() {
        return humidity;
    }
    public int getPressure() {
        return pressure;
    }
    public double getLat() { return lat; }
    public double getLon() { return lon; }
}