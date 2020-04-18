package ru.bdim.weather.data;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.bdim.weather.R;
import ru.bdim.weather.addiyional.Constants;
import ru.bdim.weather.application.WeatherApp;
import ru.bdim.weather.internet.Convertable;
import ru.bdim.weather.internet.ForecastRequest;
import ru.bdim.weather.internet.WeatherRequest;

public class InternetWeather extends Service implements Constants {

    private IBinder binder = new ServiceBinder();
    private String weather_api_key;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        weather_api_key = getResources().getString(R.string.weather_api_key);
        return binder;
    }
    public class ServiceBinder extends Binder {

        private static final int COUNT = 8;

        InternetWeather getService(){
            return InternetWeather.this;
        }

        public void getCurrentWeatherByCity(String city, String lang, ConnectionListener listener) {
            Call<WeatherRequest> call = WeatherApp.getInstance().getInternet()
                    .loadWeatherByCity(city, "metric", weather_api_key, lang);
            getService().getWeatherRetrofit(call, listener);
        }
        public void getCurrentWeatherByCoord(double lat, double lon, String lang, ConnectionListener listener) {
            Call<WeatherRequest> call = WeatherApp.getInstance().getInternet()
                    .loadWeatherByCoord(lat, lon, "metric", weather_api_key, lang);
            getService().getWeatherRetrofit(call, listener);
        }

        public void getHourlyForecastByCity(String city, ConnectionListener listener) {
            Call<ForecastRequest> call = WeatherApp.getInstance().getInternet()
                    .loadForecastByCity(COUNT, city, "metric", weather_api_key);
            getService().getForecastRetrofit(call, listener);
        }
        public void getHourlyForecastByCoord(double lat, double lon, ConnectionListener listener) {
            Call<ForecastRequest> call = WeatherApp.getInstance().getInternet()
                    .loadForecastByCoord(COUNT, lat, lon, "metric", weather_api_key);
            getService().getForecastRetrofit(call, listener);

        }
    }
    public interface ConnectionListener {
        void gettingComplete(Convertable request);
        void errorConnection();
    }
    public void getWeatherRetrofit(Call<WeatherRequest> call, final ConnectionListener listener) {
        call.enqueue(new Callback<WeatherRequest>() {
            @Override
            public void onResponse(Call<WeatherRequest> call, Response<WeatherRequest> response) {
                WeatherRequest weatherRequest = response.body();
                if (weatherRequest != null){
                    listener.gettingComplete(weatherRequest);
                } else {
                    listener.errorConnection();
                }
            }
            @Override
                public void onFailure(Call<WeatherRequest> call, Throwable t) {
                    listener.errorConnection();
                }
        });
    }
    public void getForecastRetrofit(Call<ForecastRequest> call, final ConnectionListener listener) {
        call.enqueue(new Callback<ForecastRequest>() {
                    @Override
                    public void onResponse(Call<ForecastRequest> call, Response<ForecastRequest> response) {
                        ForecastRequest request = response.body();
                        if (request != null){
                            Log.d(TAG, response.body().toString());
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