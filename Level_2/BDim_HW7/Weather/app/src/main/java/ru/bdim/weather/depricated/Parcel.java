package ru.bdim.weather.depricated;

import android.os.Parcelable;

import java.io.IOException;

public class Parcel {//implements Parcelable {
    //это лишний класс!!!
    //надо оставить парсел-погоду, а булеоны передавать отдельно!!!
//    private CurrentWeather weather;
//    private boolean isExtra;
//    private boolean isSunAndMoon;
//
//    public Parcel(String city, String lang, CurrentWeather.ConnectionListener listener) {
//        this.weather = new CurrentWeather(city, lang, listener);
//        this.isExtra = false;
//        this.isSunAndMoon = false;
//    }
//
//    protected Parcel(android.os.Parcel in) {
//        weather = in.readParcelable(CurrentWeather.class.getClassLoader());
//        isExtra = in.readByte() != 0;
//        isSunAndMoon = in.readByte() != 0;
//    }
//
//    public static final Creator<Parcel> CREATOR = new Creator<Parcel>() {
//        @Override
//        public Parcel createFromParcel(android.os.Parcel in) {
//            return new Parcel(in);
//        }
//
//        @Override
//        public Parcel[] newArray(int size) {
//            return new Parcel[size];
//        }
//    };
//
//    public String getCity() {
//        return weather.getCity();
//    }
//    //рассмотреть необходимость этого метода
//    //это может подождать
////    public void setCity(String city){
////        weather.refresh(city);
////    }
//
//    public CurrentWeather getWeather() { return weather; }
//
//    public void setWeather(CurrentWeather weather) { this.weather = weather; }
//
//    public boolean isExtra() {
//        return isExtra;
//    }
//    public boolean isSunAndMoon() {
//        return isSunAndMoon;
//    }
//
//    public void setParameters(boolean extra, boolean sunMoon) {
//        this.isExtra = extra;
//        this.isSunAndMoon = sunMoon;
//    }
//
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(android.os.Parcel dest, int flags) {
//        dest.writeParcelable(weather, flags);
//        dest.writeByte((byte) (isExtra ? 1 : 0));
//        dest.writeByte((byte) (isSunAndMoon ? 1 : 0));
//    }
}