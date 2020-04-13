package ru.bdim.weather.data;

import java.util.ArrayList;
import java.util.List;

import ru.bdim.weather.application.WeatherApp;
import ru.bdim.weather.databases.HistoryEntity;

class DatabaseWeather {

    public static List<CurrentWeather> getWeatherList() {
        return convertToWeatherList(WeatherApp.getInstance().getDao().getAll());
    }
    private static List<CurrentWeather> convertToWeatherList(List<HistoryEntity> historyList) {
        List<CurrentWeather> weather = new ArrayList<>();
        for (HistoryEntity history : historyList) {
            weather.add(new CurrentWeather(history));
        }
        return weather;
    }

    public static List<CurrentWeather> getSortedByCityList() {
        return convertToWeatherList(WeatherApp.getInstance().getDao().sortByCity());
    }

    public static List<CurrentWeather> getSortedByDateList() {
        return convertToWeatherList(WeatherApp.getInstance().getDao().sortByDate());
    }

    public static List<CurrentWeather> getSortedByTempList() {
        return convertToWeatherList(WeatherApp.getInstance().getDao().sortByTemp());
    }

    public static List<CurrentWeather> search(String query) {
        return convertToWeatherList(WeatherApp.getInstance().getDao().search(query + "%"));
    }

    public static List<CurrentWeather> remove(String city) {
        WeatherApp.getInstance().getDao().removeHistory(city);
        return convertToWeatherList(WeatherApp.getInstance().getDao().getAll());
    }

    public static List<CurrentWeather> updateWeather(CurrentWeather weather) {
        HistoryEntity history = new HistoryEntity(weather);
        WeatherApp.getInstance().getDao().updateHistory(history);
        return getWeatherList();
    }
    public static List<CurrentWeather> insertWeather(CurrentWeather weather) {
        HistoryEntity history = new HistoryEntity(weather);
        WeatherApp.getInstance().getDao().insertHistory(history);
        return getWeatherList();
    }
}