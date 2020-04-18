package ru.bdim.weather.data;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import ru.bdim.weather.R;

import ru.bdim.weather.internet.Convertable;
import ru.bdim.weather.internet.ForecastRequest;
import ru.bdim.weather.internet.WeatherRequest;

import static ru.bdim.weather.addiyional.Constants.TAG;

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
    private InternetWeather.ConnectionListener connectionListener;

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
    public static void refresh(Context context, String city) {
        instance.dataListener.loading();
        final String lang = context.getResources().getString(R.string.lang);
        if (instance.binder != null) {
            if (city != null) {
                instance.binder.getCurrentWeatherByCity(city, lang, instance.connectionListener);
                instance.binder.getHourlyForecastByCity(city, instance.connectionListener);
            }
        }
    }
    private static void addCityToList() {
        for (CurrentWeather w : instance.weatherList) {
            if (w.getCity().equals(instance.currentWeather.getCity())) {
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
        instance.dataListener.loading();
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
    public static void setListeners(DataListener listener) {

        instance.dataListener = listener;
        if (instance.dataListener != null){
            instance.connectionListener  = new InternetWeather.ConnectionListener() {
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
                        count = 0;
                    }
                }
            };
        } else {
            instance.connectionListener = null;
        }
    }
    public interface DataListener {
        void loading();
        void receivingSuccess();
        void receivingError();
    }
}