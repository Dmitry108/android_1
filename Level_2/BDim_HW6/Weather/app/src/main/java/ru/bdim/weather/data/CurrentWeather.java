package ru.bdim.weather.data;

import android.util.Log;

import java.security.SecureRandom;
import java.util.Date;

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

    public CurrentWeather(){}
    public CurrentWeather(HistoryEntity history){
        setDatabaseData(history);
    }
    public CurrentWeather(WeatherRequest request) {
        setHttpsData(request);
    }

    public void setTestData(String city){
        final SecureRandom rn = new SecureRandom();
        this.city = city;
        this.date = Format.dateToString(new Date());
        this.temperature = rn.nextInt(61) - 30;
        this.sky = rn.nextInt(temperature < 0 ? 6 : 5);
        this.skyDescription = "BatteryReceiver";
        this.windSpeed = rn.nextFloat();
        this.windDir = windSpeed == 0 ? 0 : rn.nextInt(8) + 1;
        this.humidity = rn.nextInt(101);
        this.pressure = rn.nextInt(101) + 700;
    }
    public void setHttpsData(WeatherRequest request) {
        this.city = request.getCity();
        this.date = request.getDate();
        this.temperature = request.getTemperature();
        this.sky = Format.getIconNumber(request.getSky());
        this.skyDescription = request.getSkyDescription();
        this.windSpeed = request.getWindSpeed();
        this.windDir = 5;//request.getWindDir();
        this.humidity = request.getHumidity();
        this.pressure = request.getPressure();
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
}