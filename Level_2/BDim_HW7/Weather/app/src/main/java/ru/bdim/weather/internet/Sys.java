package ru.bdim.weather.internet;

import com.google.gson.annotations.SerializedName;

public class Sys {
    @SerializedName("type")
    private int type;
    @SerializedName("id")
    private int id;
    @SerializedName("country")
    private String country;
    @SerializedName("sunrise")
    private long sunrise;
    @SerializedName("sunset")
    private long sunset;
    @SerializedName("pod")
    private String pod;
}