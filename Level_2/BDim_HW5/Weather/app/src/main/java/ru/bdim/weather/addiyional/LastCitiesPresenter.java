package ru.bdim.weather.addiyional;

import java.util.ArrayList;
import java.util.List;

import ru.bdim.weather.application.WeatherApp;
import ru.bdim.weather.databases.HistoryEntity;

public class LastCitiesPresenter {
    private static LastCitiesPresenter instance = null;
    //Weather предстваляет данные, к которым обращается приложение,
    //и, пока, является посредником работы с интернетом
    //пометка для себя: ВЫДЕЛИТЬ РАБОТУ С ИНТЕРНЕТОМ В ОТДЕЛЬНЫЙ КЛАСС
    private ArrayList<Weather> weatherList;

    private LastCitiesPresenter(){
        weatherList = new ArrayList<>();
        List<HistoryEntity> historyList = WeatherApp.getInstance().getDao().getAll();
        setWeatherList(historyList);
    }
    public static LastCitiesPresenter getInstance(){
        if (instance == null) {
            instance = new LastCitiesPresenter();
        }
        return instance;
    }

    public ArrayList<Weather> getWeatherList() {
        return weatherList;
    }
    public void setWeatherList(List<HistoryEntity> historyList) {
        weatherList.clear();
        for (HistoryEntity history: historyList){
            weatherList.add(getWeatherFromHistory(history));
        }
    }
    //Методы конвертеры представления данных между приложением и POJO-классами для базы данный
    private Weather getWeatherFromHistory(HistoryEntity history){
        return new Weather(history.getCity(), history.getDate(), history.getTemperature(),
                history.getSky(), history.getSkyDescription(), history.getWindSpeed(), history.getWindDir(),
                history.getHumidity(), history.getPressure());
    }
    private HistoryEntity getHistoryFromWeather(Weather weather){
        return new HistoryEntity(weather.getCity(), weather.getDate(), weather.getTemperature(),
                weather.getSkyIcon(), weather.getSkyDescription(), weather.getWindSpeed(),
                weather.getWindDir(), weather.getHumidity(), weather.getPressure());
    }

    //добавление элемента в список и базу данных
    public void addCityToList(Weather weather) {
        for (Weather w : weatherList) {
            if (w.getCity().equals(weather.getCity())) {
                weatherList.remove(w);
                WeatherApp.getInstance().getDao().updateHistory(getHistoryFromWeather(w));
                break;
            } else {
                WeatherApp.getInstance().getDao().insertHistory(getHistoryFromWeather(w));
            }
        }
        weatherList.add(0, weather);
    }
    public void removeItem(int position){
        WeatherApp.getInstance().getDao().removeHistory(weatherList.get(position).getCity());
        weatherList.remove(position);
    }
    public void sortByCity(){
        setWeatherList(WeatherApp.getInstance().getDao().sortByCity());
    }

    public void sortByDate() {
        setWeatherList(WeatherApp.getInstance().getDao().sortByDate());
    }

    public void sortByTemp() {
        setWeatherList(WeatherApp.getInstance().getDao().sortByTemp());
    }
}