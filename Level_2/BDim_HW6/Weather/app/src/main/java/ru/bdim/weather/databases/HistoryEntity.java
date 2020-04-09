package ru.bdim.weather.databases;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Date;

import ru.bdim.weather.data.CurrentWeather;

@Entity(indices = @Index(value = {"city"}, unique = true))
public class HistoryEntity {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;
    @ColumnInfo(name = "city")
    private String city;
    @ColumnInfo(name = "date")
    private String date;
    @ColumnInfo(name = "temperature")
    private int temperature;
    @ColumnInfo(name = "sky")
    private int sky;
    @ColumnInfo(name = "sky_description")
    private String skyDescription;
    @ColumnInfo(name = "wind_speed")
    private float windSpeed;
    @ColumnInfo(name = "wind_dir")
    private int windDir;
    @ColumnInfo(name = "humidity")
    private int humidity;
    @ColumnInfo(name = "pressure")
    private int pressure;

    public HistoryEntity(String city, String date, int temperature,
                         int sky, String skyDescription, float windSpeed,
                         int windDir, int humidity, int pressure) {
        this.city = city;
        this.date = date;
        this.temperature = temperature;
        this.sky = sky;
        this.skyDescription = skyDescription;
        this.windSpeed = windSpeed;
        this.windDir = windDir;
        this.humidity = humidity;
        this.pressure = pressure;
    }
    public HistoryEntity(CurrentWeather weather) {
        this.city = weather.getCity();
        this.date = weather.getDate();
        this.temperature = weather.getTemperature();
        this.sky = weather.getSky();
        this.skyDescription = weather.getSkyDescription();
        this.windSpeed = weather.getWindSpeed();
        this.windDir = weather.getWindDir();
        this.humidity = weather.getHumidity();
        this.pressure = weather.getPressure();
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public int getTemperature() {
        return temperature;
    }
    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }
    public int getSky() {
        return sky;
    }
    public void setSky(int sky) {
        this.sky = sky;
    }
    public String getSkyDescription() {
        return skyDescription;
    }
    public void setSkyDescription(String skyDescription) {
        this.skyDescription = skyDescription;
    }
    public float getWindSpeed() {
        return windSpeed;
    }
    public void setWindSpeed(float windSpeed) {
        this.windSpeed = windSpeed;
    }
    public int getWindDir() {
        return windDir;
    }
    public void setWindDir(int windDir) {
        this.windDir = windDir;
    }
    public int getHumidity() {
        return humidity;
    }
    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }
    public int getPressure() {
        return pressure;
    }
    public void setPressure(int pressure) {
        this.pressure = pressure;
    }
}