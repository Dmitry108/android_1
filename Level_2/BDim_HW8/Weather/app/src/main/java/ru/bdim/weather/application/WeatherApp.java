package ru.bdim.weather.application;

import android.app.Application;
import android.content.Context;

import androidx.room.Room;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.bdim.weather.addiyional.Constants;
import ru.bdim.weather.data.Weather;
import ru.bdim.weather.databases.HistoryDao;
import ru.bdim.weather.databases.HistoryDatabase;
import ru.bdim.weather.internet.OpenWeather;

public class WeatherApp extends Application implements Constants {
    private static WeatherApp instance;

    private HistoryDatabase history;
    private OpenWeather openWeather;

//    private RefWatcher refWatcher;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        //база данных
        history = Room.databaseBuilder(getApplicationContext(), HistoryDatabase.class, "history.db")
                .allowMainThreadQueries()
                .build();
        //интернет
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(WEATHER_BASE_URL_retrofit)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        openWeather = retrofit.create(OpenWeather.class);

        //инициализация координатора данных
        Weather.init();
//        setUpLeakCanary();
    }

//    private void setUpLeakCanary() {
//        if (LeakCanary.isInAnalyzerProcess(this)){
//            return;
//        }
//        refWatcher = LeakCanary.install(this);
//    }
//    public static RefWatcher getRefWatcher(Context context) {
//        return instance.refWatcher;
//    }

    public static WeatherApp getInstance(){
        return instance;
    }

    public HistoryDao getDao(){
        return history.getHistoryDao();
    }
    public OpenWeather getInternet(){
        return openWeather;
    }
}