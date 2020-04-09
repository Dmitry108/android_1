package ru.bdim.weather.patterns;

import ru.bdim.weather.data.Data;

public interface Observer {
    void updateWeather(Data data);
}