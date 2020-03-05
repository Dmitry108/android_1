package ru.bdim.weather;

import android.os.Handler;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;

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
    private static final String WEATHER_URL =
            "https://api.openweathermap.org/data/2.5/weather?lat=55.75&lon=37.62&appid=";
    private static final String WEATHER_API_KEY = "6bdcb2b7e6c07df82dc5d96dc7b9ab2e";
    private static Data data;

    public static void refresh() {
        try {
            final URL uri = new URL(String.format("%s%s", WEATHER_URL, WEATHER_API_KEY));
            final Handler handler = new Handler(); // Запоминаем основной поток
            new Thread(new Runnable() {
                public void run() {
                    HttpsURLConnection urlConnection = null;
                    try {
                        urlConnection = (HttpsURLConnection) uri.openConnection();
                        urlConnection.setRequestMethod("GET"); // установка метода получения данных -GET
                        urlConnection.setReadTimeout(10000); // установка таймаута - 10 000 миллисекунд
                        //Не могу понять почему в этом месте возникает ошибка и приложение закрывается
                        BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream())); // читаем  данные в поток
                        final String result = getLines(in);
                        // преобразование данных запроса в модель
                        Gson gson = new Gson();
                        final Data weatherRequest = gson.fromJson(result, Data.class);
                        // Возвращаемся к основному потоку
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                data = weatherRequest;
                            }
                        });
                    } catch (Exception e) {
                        Log.e(TAG, "Fail connection", e);
                        e.printStackTrace();
                    } finally {
                        if (null != urlConnection) {
                            urlConnection.disconnect();
                        }
                    }
                }
            }).start();
        } catch (MalformedURLException e) {
            Log.e(TAG, "Fail URI", e);
            e.printStackTrace();
        }
    }

    private static String getLines(BufferedReader in) {
        return in.lines().collect(Collectors.joining("\n"));
    }

    public static Data getData() {
        if (data == null) {
            refresh();
        }
        return data;
    }
}