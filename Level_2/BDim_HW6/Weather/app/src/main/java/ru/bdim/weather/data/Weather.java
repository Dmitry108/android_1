package ru.bdim.weather.data;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import ru.bdim.weather.internet.Convertable;
import ru.bdim.weather.internet.ForecastRequest;
import ru.bdim.weather.internet.WeatherRequest;

public class Weather {
//центральный класс-синглтон содержащий ссылки на классы-представители данных для фрагментов.
//обеспечиваает обмен данными между этими классами
// и классами-преставителями источников данных (InternetWeather и DatabaseWeather)

    private static Weather instance;
    private String city;
    private CurrentWeather currentWeather;
    private HourlyForecast hourlyForecast;
    private List<CurrentWeather> weatherList;
    private InternetWeather.ServiceBinder binder;

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
    public static void refresh(String city, String lang, final DataListener dataListener) {
        instance.city = city;
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
        InternetWeather.ConnectionListener listener  = new InternetWeather.ConnectionListener() {
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
                dataListener.receivingError();
            }
            private void checkData(){
                if (count == 2) {
                    dataListener.receivingSuccess();
                    addCityToList();
                }
            }
        };
        if (instance.binder != null){
            instance.binder.getCurrentWeather(city, lang, listener);
            instance.binder.getHourlyForecast(city, listener);
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
        return instance.city;
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

    public static void loadLast(String city, DataListener listener) {
        List<CurrentWeather> list = DatabaseWeather.search(city);
        if (list.size() != 0) {
            instance.currentWeather = list.get(0);
            instance.hourlyForecast.getHourlyWeatherList().clear();
            listener.receivingSuccess();
        } else {
            listener.receivingError();
        }
    }

    public interface DataListener {
        void receivingSuccess();
        void receivingError();
    }
}