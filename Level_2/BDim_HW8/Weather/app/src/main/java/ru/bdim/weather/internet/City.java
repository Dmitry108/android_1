package ru.bdim.weather.internet;

import com.google.gson.annotations.SerializedName;

public class City {
    @SerializedName("id")
    private long id;
    @SerializedName("name")
    private String name;
    @SerializedName("coord")
    private Coord coord;
    @SerializedName("country")
    private String country;
    @SerializedName("population")
    private int population;
    @SerializedName("timezone")
    private long timezone;
    @SerializedName("sunrise")
    private long sunrise;
    @SerializedName("sunset")
    private long sunset;

    public long getTimezone(){
        return timezone;
    }
}
