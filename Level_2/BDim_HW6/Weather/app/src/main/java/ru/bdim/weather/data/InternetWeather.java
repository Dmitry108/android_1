package ru.bdim.weather.data;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.bdim.weather.addiyional.Constants;
import ru.bdim.weather.internet.Convertable;
import ru.bdim.weather.internet.ForecastRequest;
import ru.bdim.weather.internet.OpenWeather;
import ru.bdim.weather.internet.WeatherRequest;

public class InternetWeather extends Service implements Constants {

    //Александр! Подскажите, какой вариант решение более оптимальный:
    // - использовать привязанный сервис и возвращать результат при помощи механизма IBinder
    // - или использовать запущенный сервис с обработкой в onStartCommand() и возвращать результат
    //      через Broadcast?
    // - или существуют и более оптимальные решения?

    private static final String WEATHER_URL = "https://api.openweathermap.org/data/2.5/weather?";
    private static final String HOURLY_URL = "https://api.openweathermap.org/data/2.5/forecast?cnt=8&";
    private static final String WEATHER_API_KEY = "&appid=6bdcb2b7e6c07df82dc5d96dc7b9ab2e";

    //для retrofit
    private static final String WEATHER_BASE_URL_retrofit = "https://api.openweathermap.org/";
    private static final String WEATHER_API_KEY_retrofit = "6bdcb2b7e6c07df82dc5d96dc7b9ab2e";

    private IBinder binder = new ServiceBinder();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "создание сервиса");
    }
    public InternetWeather() {
        super();
        Log.d(TAG, "конструктор сервиса");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }
    public class ServiceBinder extends Binder {
        InternetWeather getService(){
            return InternetWeather.this;
        }

        public void getCurrentWeather(String city, String lang, ConnectionListener listener) {
//            String uri = String.format("%sq=%s&units=metric%s&lang=%s", WEATHER_URL, city,
//                    WEATHER_API_KEY, lang);
//            getHttps(uri, WeatherRequest.class, listener);
            getService().getWeatherRetrofit(city, lang, listener);
        }
        public void getHourlyForecast(String city, ConnectionListener listener) {
//            String uri = String.format("%sq=%s&units=metric%s", HOURLY_URL, city,
//                    WEATHER_API_KEY);
//            getService().getHttps(uri, ForecastRequest.class, listener);
            getService().getForecastRetrofit(city, listener);
        }
    }

    public void getHttps(String uri, final Class<? extends Convertable> T, final ConnectionListener listener) {
        try {
            final Handler handler = new Handler();
            Log.d(TAG, uri+"\n");
            final URL url = new URL(uri);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    HttpsURLConnection https = null;
                    try {
                        https = (HttpsURLConnection) url.openConnection();
                        https.setRequestMethod("GET");
                        https.setReadTimeout(10000);
                        BufferedReader in = new BufferedReader(new InputStreamReader(https.getInputStream()));
                        final StringBuilder buf = new StringBuilder();
                        String line;
                        while ((line = in.readLine()) != null) {
                            buf.append(line).append(System.getProperty("line.separator"));
                            Log.d(TAG, line+"\n");
                        }
                        final Gson gson = new Gson();
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                listener.gettingComplete(gson.fromJson(buf.toString(), T));
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if (https != null){
                            https.disconnect();
                        }
                    }
                }
            }).start();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public interface ConnectionListener {
        void gettingComplete(Convertable request);
        void errorConnection();
    }

    //подумать над обобщенным вызовом
    public void getWeatherRetrofit(String city, String lang, final ConnectionListener listener) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(WEATHER_BASE_URL_retrofit)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofit.create(OpenWeather.class)
                .loadCurrentWeather(city, "metric", WEATHER_API_KEY_retrofit, lang)
                .enqueue(new Callback<WeatherRequest>() {
                    @Override
                    public void onResponse(Call<WeatherRequest> call, Response<WeatherRequest> response) {
                        WeatherRequest weatherRequest = response.body();
                        if (weatherRequest != null){
                            listener.gettingComplete(weatherRequest);
                        }
                    }
                    @Override
                    public void onFailure(Call<WeatherRequest> call, Throwable t) {
                        listener.errorConnection();
                    }
                });
    }
    public void getForecastRetrofit(String city, final ConnectionListener listener) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(WEATHER_BASE_URL_retrofit)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofit.create(OpenWeather.class)
                .loadHourlyForecast(8,city, "metric", WEATHER_API_KEY_retrofit)
                .enqueue(new Callback<ForecastRequest>() {
                    @Override
                    public void onResponse(Call<ForecastRequest> call, Response<ForecastRequest> response) {
                        ForecastRequest request = response.body();
                        if (request != null){
                            listener.gettingComplete(request);
                        }
                    }
                    @Override
                    public void onFailure(Call<ForecastRequest> call, Throwable t) {
                        listener.errorConnection();
                    }
                });
    }
}