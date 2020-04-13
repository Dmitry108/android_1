package ru.bdim.weather.internet;

import com.google.gson.annotations.SerializedName;

public class ForecastRequest implements Convertable {

    @SerializedName("cod")
    private int cod; //"200"
    @SerializedName("message")
    private int message; //0
    @SerializedName("cnt")
    private int cnt; //3
    @SerializedName("list")
    private Forecast[] list;
    @SerializedName("city")
    private City city;

    public Forecast[] getForecastArray(){
        return list;
    }
}
