package ru.bdim.weather.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ru.bdim.weather.addiyional.Format;
import ru.bdim.weather.internet.Forecast;
import ru.bdim.weather.internet.ForecastRequest;

public class HourlyForecast implements Data {
    private List<HourlyWeather> hourlyList;

    public HourlyForecast(){
        this.hourlyList = new ArrayList<>();
    }

    public HourlyForecast(ForecastRequest request){
        this.hourlyList = new ArrayList<>();
        for (Forecast f: request.getForecastArray()){
            hourlyList.add(new HourlyWeather(new Date((f.getDate() + request.getTimezone())*1000L),
                    f.getTemperature(), Format.getIconNumber(f.getSky())));
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