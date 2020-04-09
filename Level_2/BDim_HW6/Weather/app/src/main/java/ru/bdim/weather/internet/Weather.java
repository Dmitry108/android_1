package ru.bdim.weather.internet;

import com.google.gson.annotations.SerializedName;

public class Weather {
    @SerializedName("id")
    private int id;
    @SerializedName("main")
    private String main;
    @SerializedName("description")
    private String description;
    @SerializedName("icon")
    private String icon;

    public String getIcon() {
        return icon;
    }
    public String getDescription() {
        return description;
    }
    public String getMain(){return main;}
}