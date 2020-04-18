package ru.bdim.weather.internet;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.bdim.weather.addiyional.Constants;

public interface OpenWeather {
    @GET(Constants.WEATHER_CURRENT)
    Call<WeatherRequest> loadWeatherByCity(@Query("q") String city,
                                           @Query("units") String units,
                                           @Query("appid") String keyApi,
                                           @Query("lang") String lang);
    @GET(Constants.WEATHER_CURRENT)
    Call<WeatherRequest> loadWeatherByCoord(@Query("lat") double lat,
                                            @Query("lon") double lon,
                                            @Query("units") String units,
                                            @Query("appid") String keyApi,
                                            @Query("lang") String lang);
    @GET(Constants.WEATHER_FORECAST)
    Call<ForecastRequest> loadForecastByCity(@Query("cnt") int number,
                                             @Query("q") String city,
                                             @Query("units") String units,
                                             @Query("appid") String keyApi);
    @GET(Constants.WEATHER_FORECAST)
    Call<ForecastRequest> loadForecastByCoord(@Query("cnt") int number,
                                              @Query("lat") double lat,
                                              @Query("lon") double lon,
                                              @Query("units") String units,
                                              @Query("appid") String keyApi);
}