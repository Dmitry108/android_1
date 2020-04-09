package ru.bdim.weather.application;

import android.app.Application;
import androidx.room.Room;
import ru.bdim.weather.databases.HistoryDao;
import ru.bdim.weather.databases.HistoryDatabase;

public class WeatherApp extends Application {
    private static WeatherApp instance;
    private HistoryDatabase history;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        history = Room.databaseBuilder(getApplicationContext(), HistoryDatabase.class, "history.db")
                .allowMainThreadQueries()
                .build();
    }
    public static WeatherApp getInstance(){
        return instance;
    }
    public HistoryDao getDao(){
        return history.getHistoryDao();
    }
}