package ru.bdim.weather.addiyional;

import android.os.Parcelable;

import java.io.IOException;

public class Parcel implements Parcelable {
    private Weather weather;
    private boolean isExtra;
    private boolean isSunAndMoon;

    public Parcel(String city, String lang, Weather.ConnectionListener listener) {
        this.weather = new Weather(city, lang, listener);
        this.isExtra = false;
        this.isSunAndMoon = false;
    }

    protected Parcel(android.os.Parcel in) {
        weather = in.readParcelable(Weather.class.getClassLoader());
        isExtra = in.readByte() != 0;
        isSunAndMoon = in.readByte() != 0;
    }

    public static final Creator<Parcel> CREATOR = new Creator<Parcel>() {
        @Override
        public Parcel createFromParcel(android.os.Parcel in) {
            return new Parcel(in);
        }

        @Override
        public Parcel[] newArray(int size) {
            return new Parcel[size];
        }
    };

    public String getCity() {
        return weather.getCity();
    }
    //рассмотреть необходимость этого метода
    //это может подождать
//    public void setCity(String city){
//        weather.refresh(city);
//    }

    public Weather getWeather() { return weather; }

    public void setWeather(Weather weather) { this.weather = weather; }

    public boolean isExtra() {
        return isExtra;
    }

    public void setParameters(boolean extra, boolean sunMoon) {
        this.isExtra = extra;
        this.isSunAndMoon = sunMoon;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeParcelable(weather, flags);
        dest.writeByte((byte) (isExtra ? 1 : 0));
        dest.writeByte((byte) (isSunAndMoon ? 1 : 0));
    }
}