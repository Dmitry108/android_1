package ru.bdim.weather2.additional;

import android.os.Parcelable;

public class Parcel implements Parcelable{//Serializable {
    private Weather weather;
    private boolean isExtra;
    private boolean isSunAndMoon;

    public Parcel(String city, String date){
        this.weather = new Weather(city, date);
        this.isExtra = false;
        this.isSunAndMoon = false;
    }
    public Parcel(Weather weather){
        this.weather = weather;
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
    public void setCity(String city, String date){
        weather.refresh(city, date);
    }

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