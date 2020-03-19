package ru.bdim.weather;

import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.stream.Collectors;

import javax.net.ssl.HttpsURLConnection;

import ru.bdim.weather.data.Data;

public class WeatherData {
    private static final String TAG = "WEATHER";
    private static final String WEATHER_URL = "https://api.openweathermap.org/data/2.5/weather?lat=55.75&lon=37.62&appid=";
    private static final String WEATHER_API_KEY = "6bdcb2b7e6c07df82dc5d96dc7b9ab2e";
    //private Data data;
    private double temperature;
    private int windSpeed;
    private int windDeg;
    private int humidity;
    private int pressure;

    public WeatherData() {
        refresh();
    }

    public void refresh() {
        try {
            final URL uri = new URL(String.format("%s%s", WEATHER_URL, WEATHER_API_KEY));

            HttpsURLConnection urlConnection = null;
            try {
                urlConnection = (HttpsURLConnection) uri.openConnection();
                urlConnection.setRequestMethod("GET"); // установка метода получения данных -GET
                urlConnection.setReadTimeout(10000); // установка таймаута - 10 000 миллисекунд

                BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream())); // читаем  данные в поток
                final String result = getLines(in);
                // преобразование данных запроса в модель
                Gson gson = new Gson();

                final Data weatherRequest = gson.fromJson(result, Data.class);

                temperature = weatherRequest.getMain().getTemperature();
                windSpeed = weatherRequest.getWind().getSpeed();
                windDeg = weatherRequest.getWind().getDeg();
                humidity = weatherRequest.getMain().getHumidity();
                pressure = weatherRequest.getMain().getPressure();
            } catch (Exception e) {
                Log.e(TAG, "Fail connection", e);
                e.printStackTrace();
            } finally {
                if (null != urlConnection) {
                    urlConnection.disconnect();
                }
            }
        } catch (MalformedURLException e) {
            Log.e(TAG, "Fail URI", e);
            e.printStackTrace();
        }
    }

    private static String getLines(BufferedReader in) {
        return in.lines().collect(Collectors.joining("\n"));
    }

    public double getTemperature() {
        return temperature;
    }

    public int getWindSpeed() {
        return windSpeed;
    }

    public int getWindDeg() {
        return windDeg;
    }

    public int getHumidity() {
        return humidity;
    }

    public int getPressure() {
        return pressure;
    }
}