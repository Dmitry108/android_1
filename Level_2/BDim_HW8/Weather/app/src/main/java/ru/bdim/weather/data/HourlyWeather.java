package ru.bdim.weather.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

import ru.bdim.weather.addiyional.Format;

public class HourlyWeather implements Parcelable {
    private Date date;
    private int temperature;
    private int sky;

    public HourlyWeather(Date date, int temperature, int sky) {
        this.date = date;
        this.temperature = temperature;
        this.sky = sky;
    }

    protected HourlyWeather(Parcel in) {
        date = Format.stringToDate(in.readString());
        temperature = in.readInt();
        sky = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Format.dateToString(date));
        dest.writeInt(temperature);
        dest.writeInt(sky);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<HourlyWeather> CREATOR = new Creator<HourlyWeather>() {
        @Override
        public HourlyWeather createFromParcel(Parcel in) {
            return new HourlyWeather(in);
        }

        @Override
        public HourlyWeather[] newArray(int size) {
            return new HourlyWeather[size];
        }
    };

    public Date getDate() {
        return date;
    }
    public int getTemperature() {
        return temperature;
    }
    public int getSky() {
        return sky;
    }
}