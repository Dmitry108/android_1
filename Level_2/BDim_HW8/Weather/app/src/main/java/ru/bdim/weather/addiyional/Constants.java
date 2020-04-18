package ru.bdim.weather.addiyional;

public interface Constants {
    String CITY = "city";
    String EXTRA = "extra";
    String HOURLY = "hourly";
    String BACKGROUND = "background";
    String LOCATION = "location";
    String COORD = "@coord";
    String OPEN_MAP = "@map";

    String WEATHER_BASE_URL_retrofit = "https://api.openweathermap.org/";
    String WEATHER_CURRENT = "data/2.5/weather";
    String WEATHER_FORECAST = "data/2.5/forecast";

    int ENTER_CITY_RESULT_CODE = 1;
    int SELECT_CITY_REQUEST_CODE = 2;
    int SELECT_CITY_RESULT_CODE = 2;
    int BACKGROUND_RESULT_CODE = 3;
    int PERMISSION_REQUEST_CODE = 4;
    int SETTINGS_RESULT_CODE = 5;
    int MAP_REQUEST_CODE = 6;
    int MAP_RESULT_CODE = 6;

    String TAG = "...";
    String ENTER_CITY_DIALOG = "enter city dialog";
    String SETTINGS_DIALOG = "settings dialog";
    String LAT = "lat";
    String LON = "lon";
}