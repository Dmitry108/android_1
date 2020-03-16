package ru.bdim.weather2.additional;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.security.SecureRandom;
import java.util.Date;

public class Weather implements Parcelable{//Serializable {
    private final SecureRandom rn = new SecureRandom();

    private String city;
    private String date;
    private int temperature;
    private int sky;
    private int windSpeed;
    private int windDir;
    private int humidity;
    private int pressure;

    public Weather(String city, String date) {
        refresh(city, date);
    }

    protected Weather(Parcel in) {
        city = in.readString();
        date = in.readString();
        temperature = in.readInt();
        sky = in.readInt();
        windSpeed = in.readInt();
        windDir = in.readInt();
        humidity = in.readInt();
        pressure = in.readInt();
    }

    public static final Creator<Weather> CREATOR = new Creator<Weather>() {
        @Override
        public Weather createFromParcel(Parcel in) {
            return new Weather(in);
        }

        @Override
        public Weather[] newArray(int size) {
            return new Weather[size];
        }
    };

    public void refresh(String city, String date) {
        this.city = city;
        this.date = date;
        temperature = rn.nextInt(61) - 30;
        sky = rn.nextInt(temperature < 0 ? 6 : 5);
        windSpeed = rn.nextInt(15);
        windDir = windSpeed == 0 ? 0 : rn.nextInt(8) + 1;
        humidity = rn.nextInt(101);
        pressure = rn.nextInt(101) + 700;
    }

    public int getTemperature() {
        return temperature;
    }

    public int getSky() {
        return sky;
    }

    public int getWindSpeed() {
        return windSpeed;
    }

    public int getWindDir() {
        return windDir;
    }

    public int getHumidity() {
        return humidity;
    }

    public int getPressure() { return pressure; }

    public String getCity() { return city; }

    public String getDate() { return date; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(city);
        dest.writeString(date);
        dest.writeInt(temperature);
        dest.writeInt(sky);
        dest.writeInt(windSpeed);
        dest.writeInt(windDir);
        dest.writeInt(humidity);
        dest.writeInt(pressure);
    }
}