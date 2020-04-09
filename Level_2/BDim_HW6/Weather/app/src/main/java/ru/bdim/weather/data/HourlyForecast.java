package ru.bdim.weather.data;

import java.lang.reflect.Array;
import java.security.SecureRandom;
import java.security.SecureRandomSpi;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;

import ru.bdim.weather.addiyional.Format;
import ru.bdim.weather.internet.Forecast;
import ru.bdim.weather.internet.ForecastRequest;

public class HourlyForecast implements Data {
    private List<HourlyWeather> hourlyList;
    private final static int COUNT = 8;

    public HourlyForecast(){
        this.hourlyList = new ArrayList<>();
    }

    public HourlyForecast(ForecastRequest request){
        this.hourlyList = new ArrayList<>();
        for (Forecast f: request.getForecastArray()){
            hourlyList.add(new HourlyWeather(new Date(f.getDate()*1000L),
                    f.getTemperature(), Format.getIconNumber(f.getSky())));
        }
    }
    public void setTestData() {
        SecureRandom rn = new SecureRandom();
        for (int i = 0; i < COUNT; i++){
            HourlyWeather weather = new HourlyWeather(
                    new Date(rn.nextLong()),
                    rn.nextInt(61) - 30,
                    rn.nextInt(7));
            hourlyList.add(weather);
        }
    }
    public List<HourlyWeather> getHourlyWeatherList() { return hourlyList;}

    public int[] getTempArray(){
        int[] tempArray = new int[hourlyList.size()];
        for (int i = 0; i < tempArray.length; i++){
            tempArray[i] = hourlyList.get(i).getTemperature();
        }
        return tempArray;
    }
}