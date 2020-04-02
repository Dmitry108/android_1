package ru.bdim.weather.patterns;

import ru.bdim.weather.addiyional.Weather;

public interface Publish {
    void subscribe(Observer observer);
    void unsubscribe(Observer observer);
    void notifyObserver(Weather weather);
}