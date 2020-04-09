package ru.bdim.weather.internet;

import com.google.gson.annotations.SerializedName;

public class Main {
    @SerializedName("temp")
    private float temp;
    @SerializedName("feels_like")
    private float feels_like;
    @SerializedName("temp_min")
    private float temp_min;
    @SerializedName("temp_max")
    private float temp_max;
    @SerializedName("pressure")
    private int pressure;
    @SerializedName("sea_level")
    private int sea_level;
    @SerializedName("grnd_level")
    private int grnd_level;
    @SerializedName("humidity")
    private int humidity;
    @SerializedName("temp_kf")
    private float temp_kf;

    public float getTemperature() {
        return temp;
    }
    public int getHumidity(){return humidity;}
    public int getPressure(){return pressure;}
}