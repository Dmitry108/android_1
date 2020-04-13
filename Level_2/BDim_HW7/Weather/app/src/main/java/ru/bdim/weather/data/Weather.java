package ru.bdim.weather.data;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ru.bdim.weather.R;
import ru.bdim.weather.addiyional.Constants;
import ru.bdim.weather.application.WeatherApp;
import ru.bdim.weather.geolocation.GeoLocation;
import ru.bdim.weather.internet.Convertable;
import ru.bdim.weather.internet.ForecastRequest;
import ru.bdim.weather.internet.WeatherRequest;

public class Weather {
//центральный класс-синглтон содержащий ссылки на классы-представители данных для фрагментов.
//обеспечиваает обмен данными между этими классами
// и классами-преставителями источников данных (InternetWeather и DatabaseWeather)

    private static Weather instance;
    private CurrentWeather currentWeather;
    private HourlyForecast hourlyForecast;
    private List<CurrentWeather> weatherList;
    private InternetWeather.ServiceBinder binder;
    private DataListener dataListener;

    public static void init() {
        if (instance == null) {
            instance = new Weather();
            instance.weatherList = new ArrayList<>();
            instance.weatherList = DatabaseWeather.getWeatherList();
        }
    }
    public static void setBinder(InternetWeather.ServiceBinder service) {
        instance.binder = service;
    }
    public static void refresh(Context context, String city, double lat, double lon) {
        final String lang = context.getResources().getString(R.string.lang);
        //instance.city = city;
        //тестовые данные
//        CurrentWeather weather = new CurrentWeather();
//        weather.setTestData(city);
//        instance.currentWeather = weather;
//        HourlyForecast forecast = new HourlyForecast();
//        forecast.setTestData();
//        instance.hourlyForecast = forecast;
//        dataListener.receivingSuccess();

        //получение данных
        //обеспечить транзакционный механизм, чтоб откатить изменения в случае ошибки
        final InternetWeather.ConnectionListener listener  = new InternetWeather.ConnectionListener() {
            private int count = 0;
            @Override
            public void gettingComplete(Convertable request) {
                if (request instanceof WeatherRequest){
                    instance.currentWeather = new CurrentWeather((WeatherRequest) request);
                    count++;
                } else if (request instanceof ForecastRequest){
                    instance.hourlyForecast = new HourlyForecast((ForecastRequest) request);
                    count++;
                }
                checkData();
            }
            @Override
            public void errorConnection() {
                instance.dataListener.receivingError();
            }

            private void checkData(){
                if (count == 2) {
                    instance.dataListener.receivingSuccess();
                    addCityToList();
                }
            }
        };
        if (instance.binder != null){
            if (city != null) {
                instance.binder.getCurrentWeatherByCity(city, lang, listener);
                instance.binder.getHourlyForecastByCity(city, listener);
            } else {
                if (lat < 0 || lon < 0){
                    GeoLocation.getCoord(context, new GeoLocation.GeoListener() {
                        @Override
                        public void coordSuccess(double lat, double lon) {
                            Log.d(Constants.TAG, String.format(Locale.ROOT, "lat %.2f, lon %.2f", lat, lon ));
                            instance.binder.getCurrentWeatherByCoord(lat, lon, lang, listener);
                            instance.binder.getHourlyForecastByCoord(lat, lon, listener);
                        }
                        @Override
                        public void coordNegative() {
                            instance.dataListener.receivingError();
                        }
                    });
                } else {
                    instance.binder.getCurrentWeatherByCoord(lat, lon, lang, listener);
                    instance.binder.getHourlyForecastByCoord(lat, lon, listener);
                }
            }
        }
    }
    private static void addCityToList() {
        for (CurrentWeather w : instance.weatherList) {
            if (w.getCity().equals(instance.currentWeather.getCity())) {
                //не работает обновление! Разобраться!!!
                instance.weatherList = DatabaseWeather.updateWeather(instance.currentWeather);
                return;
            }
        }
        instance.weatherList = DatabaseWeather.insertWeather(instance.currentWeather);
    }
    public static String getCity() {
        return instance.currentWeather.getCity();
    }

    public static HourlyForecast getHourlyForecast() {
        return instance.hourlyForecast;
    }

    public static CurrentWeather getCurrentWeather() {
        return instance.currentWeather;
    }

    public static int getWeatherListSize() {
        return instance.weatherList.size();
    }

    public static List<CurrentWeather> getWeatherList() {
        return instance.weatherList;
    }

    public static List<CurrentWeather> sortListByCity() {
        return DatabaseWeather.getSortedByCityList();
    }

    public static List<CurrentWeather> sortListByDate() {
        return DatabaseWeather.getSortedByDateList();
    }

    public static List<CurrentWeather> sortListByTemp() {
        return DatabaseWeather.getSortedByTempList();
    }

    public static List<CurrentWeather> searchList(String query) {
        return DatabaseWeather.search(query);
    }

    public static List<CurrentWeather> removeWeather(String city){
        instance.weatherList = DatabaseWeather.remove(city);
        return instance.weatherList;
    }

    public static void loadLast(String city) {
        List<CurrentWeather> list = DatabaseWeather.search(city);
        if (list.size() != 0) {
            instance.currentWeather = list.get(0);
            if (instance.hourlyForecast != null){
                instance.hourlyForecast.getHourlyWeatherList().clear();
            }
            instance.dataListener.receivingSuccess();
        } else {
            instance.dataListener.receivingError();
        }
    }

    public static void setListener(DataListener listener) {
        instance.dataListener = listener;
    }

    public interface DataListener {
        void receivingSuccess();
        void receivingError();
    }
}