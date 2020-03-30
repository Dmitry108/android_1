package ru.bdim.weather.addiyional;

import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.stream.Collectors;

import javax.net.ssl.HttpsURLConnection;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.bdim.weather.internet.OpenWeather;
import ru.bdim.weather.internet.WeatherRequest;

public class Weather implements Parcelable {
    private final SecureRandom rn = new SecureRandom();

    private String city;
    private Date date;
    private int temperature;
    private int sky;
    private String skyDescription;
    private float windSpeed;
    private int windDir;
    private int humidity;
    private int pressure;

    private static final String WEATHER_BASE_URL = "https://api.openweathermap.org/";
    private static final String WEATHER_API_KEY = "6bdcb2b7e6c07df82dc5d96dc7b9ab2e";

    private ConnectionListener listener;

    public Weather(String city, String lang, @NonNull ConnectionListener listener) {
        this.listener = listener; //продумать нужно ли оборачивать его в parcel
        refresh(city, lang);
    }

    protected Weather(Parcel in) {
        city = in.readString();
        date = (Date) in.readValue(Date.class.getClassLoader());
        temperature = in.readInt();
        sky = in.readInt();
        skyDescription = in.readString();
        windSpeed = in.readFloat();
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

    public void refresh(String city, String lang) {


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(WEATHER_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofit.create(OpenWeather.class)
                .loadWeather(city, "metric", WEATHER_API_KEY, lang)
                .enqueue(new Callback<WeatherRequest>() {
                    @Override
                    public void onResponse(Call<WeatherRequest> call, Response<WeatherRequest> response) {
                        WeatherRequest weatherRequest = response.body();
                        if (weatherRequest != null){
                            initData(weatherRequest);
                            listener.settingComplete();
                        }
                    }
                    @Override
                    public void onFailure(Call<WeatherRequest> call, Throwable t) {
                        listener.errorConnection();
                    }
                });
    }

    private void initData(WeatherRequest weatherRequest) {
        this.city = weatherRequest.getName();
        Date dateToday = new Date();

        if (date == null || !date.equals(dateToday)) {
            this.date = dateToday;
        }
        temperature = Math.round(weatherRequest.getMain().getTemperature());
        sky = getIconNumber(weatherRequest.getWeather().getIcon());//rn.nextInt(temperature < 0 ? 6 : 5);
        skyDescription = weatherRequest.getWeather().getDescription();
        windSpeed = weatherRequest.getWind().getSpeed();
        windDir = windSpeed == 0 ? 0 : rn.nextInt(8) + 1;
        humidity = weatherRequest.getMain().getHumidity();
        pressure = weatherRequest.getMain().getPressure();
    }

    private int getIconNumber(String icon) {
        int number;
        switch (icon.substring(0, 2)) {
            case "01": number = 0; break;
            case "02": number = 1; break;
            case "03":
            case "04": number = 2; break;
            case "09": number = 3; break;
            case "10": number = 4; break;
            case "11": number = 5; break;
            case "13": number = 6; break;
            case "50":
            default: number = 7;
        }
        return number;
    }

    public int getTemperature() { return temperature; }
    public int getSkyIcon() { return sky; }
    public String getSkyDescription() { return skyDescription; }
    public float getWindSpeed() { return windSpeed; }
    public int getWindDir() { return windDir; }
    public int getHumidity() { return humidity; }
    public int getPressure() { return pressure; }
    public String getCity() { return city; }
    public String getDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("d MMMM yyyy", Locale.getDefault());
        return dateFormat.format(date);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(city);
        dest.writeValue(date);
        dest.writeInt(temperature);
        dest.writeInt(sky);
        dest.writeString(skyDescription);
        dest.writeFloat(windSpeed);
        dest.writeInt(windDir);
        dest.writeInt(humidity);
        dest.writeInt(pressure);
    }
    //интерфейс для оповещения событий класса
    public interface ConnectionListener {
        void settingComplete();
        void errorConnection();
    }
}