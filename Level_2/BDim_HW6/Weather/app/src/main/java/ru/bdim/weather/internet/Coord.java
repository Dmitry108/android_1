package ru.bdim.weather.internet;

import com.google.gson.annotations.SerializedName;

public class Coord {
    @SerializedName("lat")
    private float lat;
    @SerializedName("lon")
    private float lon;
}