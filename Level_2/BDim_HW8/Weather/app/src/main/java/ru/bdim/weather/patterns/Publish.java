package ru.bdim.weather.patterns;

public interface Publish {
    void subscribe(Observer observer);
    void unsubscribe(Observer observer);
    void notifyObserver();
}