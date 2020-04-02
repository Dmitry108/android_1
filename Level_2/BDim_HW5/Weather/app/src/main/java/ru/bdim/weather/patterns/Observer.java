package ru.bdim.weather.patterns;

import ru.bdim.weather.addiyional.Weather;

public interface Observer {
    void updateWeather(Weather weather);
}