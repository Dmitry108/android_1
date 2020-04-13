package ru.bdim.weather.depricated;

import java.util.ArrayList;
import java.util.List;

import ru.bdim.weather.application.WeatherApp;
import ru.bdim.weather.databases.HistoryEntity;

public class LastCitiesPresenter {

//    //CurrentWeather предстваляет данные, к которым обращается приложение,
//    //и, пока, является посредником работы с интернетом
//    //пометка для себя: ВЫДЕЛИТЬ РАБОТУ С ИНТЕРНЕТОМ В ОТДЕЛЬНЫЙ КЛАСС
//    //Мысли: пусть этот класс будет являться связующим узлом с данными,
//    // за данными и их обработкой программа имеет возможность обратиться только сюда!!!
//    //1. все клиенты из любого места получают отсюда данные, то, есть класс нужно оставить синглтоном
//    //  созать класс на уровне аппликатион
//    //2. за данными класс обращается к классам-представителям базы данных и интернета!
//    //3. то есть, класс не только хранит данные, но и умеет их переварить, усвоить и выдать в нужной форме,
//    //  классы-представители умеют только получать данные из ресурса (или записывать туда),
//    //  а, классы-потребители - запрашивать понятные им данные
//    // Таким образом, здесь стоит выделить резервуар для хранения данных и 3 преобразователя
//    // для базы данных, интернета и потребителя
//    private static LastCitiesPresenter instance = null;
//    private ArrayList<CurrentWeather> weatherList;
//    //создать переменную текущей погоды или индекс в массиве
//
//    private LastCitiesPresenter(){
//        weatherList = new ArrayList<>();
//        List<HistoryEntity> historyList = WeatherApp.getInstance().getDao().getAll();
//        setWeatherList(historyList);
//    }
//    public static LastCitiesPresenter getInstance(){
//        if (instance == null) {
//            instance = new LastCitiesPresenter();
//        }
//        return instance;
//    }
//
//    public void setWeatherList(List<HistoryEntity> historyList) {
//        weatherList.clear();
//        for (HistoryEntity history: historyList){
//            weatherList.add(getWeatherFromHistory(history));
//        }
//    }
//
//
//    //добавление элемента в список и базу данных
//    public void addCityToList(CurrentWeather weather) {
//        for (CurrentWeather w : weatherList) {
//            if (w.getCity().equals(weather.getCity())) {
//                weatherList.remove(w);
//                WeatherApp.getInstance().getDao().removeHistory(w.getCity());
//                break;
//            }
//        }
//        WeatherApp.getInstance().getDao().insertHistory(getHistoryFromWeather(weather));
//        weatherList.add(0, weather);
//    }
//    public ArrayList<CurrentWeather> removeItem(int position){
//        WeatherApp.getInstance().getDao().removeHistory(weatherList.get(position).getCity());
//        weatherList.remove(position);
//        return weatherList;
//    }
//
//    public List<CurrentWeather> search(String query) {
//        String regex = query + "%";
//        return getListWeatherFromHistory(WeatherApp.getInstance().getDao().search(regex));
//    }
}