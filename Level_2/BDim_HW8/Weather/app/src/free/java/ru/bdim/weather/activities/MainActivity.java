package ru.bdim.weather.activities;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.res.TypedArray;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkRequest;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;
import com.squareup.leakcanary.RefWatcher;

import java.util.LinkedList;
import java.util.List;

import ru.bdim.weather.R;
import ru.bdim.weather.addiyional.Constants;
import ru.bdim.weather.application.BatteryReceiver;
import ru.bdim.weather.application.WeatherApp;
import ru.bdim.weather.data.InternetWeather;
import ru.bdim.weather.data.SettingsPreference;
import ru.bdim.weather.data.Weather;
import ru.bdim.weather.dialogs.EnterCityDialogFragment;
import ru.bdim.weather.dialogs.ErrorLoadingDialogFragment;
import ru.bdim.weather.dialogs.LoadingDialogFragment;
import ru.bdim.weather.dialogs.SettingsDialogFragment;
import ru.bdim.weather.fragments.ExtraParametersFragment;
import ru.bdim.weather.fragments.HourlyWeatherFragment;
import ru.bdim.weather.fragments.MainWeatherFragment;
import ru.bdim.weather.patterns.Observer;
import ru.bdim.weather.patterns.Publish;

public class MainActivity extends AppCompatActivity implements Constants, Publish {

    private List<Observer> ownFragments = new LinkedList<>();

    private boolean isExtra;
    private boolean isHourly;
    private boolean isConnected;

    private SettingsPreference preference;
    private ServiceConnection connection;
    private BatteryReceiver batteryReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_main);

        initMenu();
        initSystemControl();
        setBackground(0);

        MainWeatherFragment mainWeatherFragment = (MainWeatherFragment) getSupportFragmentManager().findFragmentById(R.id.fmt_main_weather);
        if (mainWeatherFragment != null) {
            subscribe(mainWeatherFragment);
        }
        initSettings();
        setOtherFragments();

        initData();
    }
    private void setOtherFragments() {
        boolean isExtraSet = false;
        boolean isHourlySet = false;

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        for (Observer o: ownFragments){
            if (o instanceof ExtraParametersFragment){
                isExtraSet = true;
                if (!isExtra){
                    ft.remove((Fragment)o);
                    unsubscribe(o);
                }
            }
            if (o instanceof HourlyWeatherFragment){
                isHourlySet = true;
                if (!isHourly){
                    ft.remove((Fragment)o);
                    unsubscribe(o);
                }
            }
        }
        if (isExtra && !isExtraSet) {
            ExtraParametersFragment fragment = new ExtraParametersFragment();
            subscribe(fragment);
            ft.replace(R.id.flt_extra_parameters, fragment);
        }
        if (isHourly && !isHourlySet) {
            HourlyWeatherFragment fragment = new HourlyWeatherFragment();
            subscribe(fragment);
            ft.replace(R.id.flt_hourly_weather, fragment);
        }
        ft.commit();
    }
    private void initSettings() {
        preference = new SettingsPreference(this);
        isExtra = preference.getSavedExtra();
        isHourly = preference.getSavedHourly();
    }
    private void initData(){
        //настройка слушателя для работы с интернетом
        Weather.DataListener listener = new Weather.DataListener(){
            DialogFragment dialog;
            @Override
            public void loading() {
                dialog = new LoadingDialogFragment();
                dialog.show(getSupportFragmentManager(), TAG);
            }
            @Override
            public void receivingSuccess() {
                dialog.dismiss();
                notifyObserver();
            }
            @Override
            public void receivingError() {
                dialog.dismiss();
                dialog = new ErrorLoadingDialogFragment();
                dialog.show(getSupportFragmentManager(), TAG);
            }
        };
        Weather.setListeners(listener);
        //привязывание к сервису для работы с интернетом
        Intent intent = new Intent(MainActivity.this, InternetWeather.class);
        connection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                Weather.setBinder((InternetWeather.ServiceBinder)service);
                setWeather(null);
            }
            @Override
            public void onServiceDisconnected(ComponentName name) {
                Weather.setBinder(null);
            }
        };
        bindService(intent, connection, BIND_AUTO_CREATE);
    }

    private void setWeather(String param){
        if (isConnected) {
            //интернет есть
            if (param == null) {
                setCityFromPref();
            } else {
                refresh(param);
            }
        } else {
            Toast.makeText(this, getResources().getString(R.string.connection_abs), Toast.LENGTH_SHORT).show();
            Weather.loadLast(preference.getSavedCity());
        }
    }

    private void setCityFromPref() {
        String city = preference.getSavedCity();
        if (city != null && !city.equals("")){
            refresh(city);
        } else {
            showEnterCityDialogFragment();
        }
    }
    private void refresh(String city){
        Weather.refresh(this, city);
    }

    private void setBackground(int background) {
        //фон
        final ScrollView layout = findViewById(R.id.lyt_main_activity);
        TypedArray imageArray = getResources().obtainTypedArray(R.array.background);
        layout.setBackground(imageArray.getDrawable(background));
        imageArray.recycle();
    }
    //Menu
    private void initMenu() {
        //панель инструментов
        Toolbar toolbar = findViewById(R.id.tlb_main);
        setSupportActionBar(toolbar);

        //боковое меню
        DrawerLayout drawer = findViewById(R.id.dlt_main);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigator = findViewById(R.id.ngv_main);
        navigator.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.mdw_enter_city:
                        showEnterCityDialogFragment();
                        break;
                    case R.id.mdw_last_cities:
                        startLastCitiesActivity();
                        break;
                    case R.id.mdw_settings:
                        showSettingsDialogFragment();
                }
                DrawerLayout drawer = findViewById(R.id.dlt_main);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }
    //Option menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_option_main, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.mob_enter_city:
                showEnterCityDialogFragment();
                return true;
            case R.id.mob_last_city:
                startLastCitiesActivity();
                return true;
            case R.id.mob_settings:
                showSettingsDialogFragment();
                return true;
        }
        return super.onContextItemSelected(item);
    }
    private void initSystemControl() {
        //отслеживание состяния сети
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkRequest nw = new NetworkRequest.Builder()
                    .build();
            cm.requestNetwork(nw, new ConnectivityManager.NetworkCallback(){
                        @Override
                        public void onAvailable(@NonNull Network network) {
                            super.onAvailable(network);
                            isConnected = true;
                            Toast.makeText(getBaseContext(), Boolean.toString(isConnected), Toast.LENGTH_SHORT).show();
                        }
                        @Override
                        public void onLost(@NonNull Network network) {
                            super.onLost(network);
                            isConnected = false;
                            Toast.makeText(getBaseContext(), Boolean.toString(isConnected), Toast.LENGTH_SHORT).show();
                        }
            });
        }
        //контроль состояния батареи
        batteryReceiver = new BatteryReceiver();
        registerReceiver(batteryReceiver, new IntentFilter("android.intent.action.BATTERY_LOW"));
    }
    private void initNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel channel = new NotificationChannel("2", "name", importance);
            notificationManager.createNotificationChannel(channel);
        }
    }
    private void showEnterCityDialogFragment() {
        EnterCityDialogFragment enterCity = new EnterCityDialogFragment();
        Bundle args = new Bundle();
        args.putBoolean(EXTRA, isExtra);
        args.putBoolean(HOURLY, isHourly);
        enterCity.setArguments(args);
        enterCity.show(getSupportFragmentManager(), ENTER_CITY_DIALOG);
    }
    private void showSettingsDialogFragment() {
        SettingsDialogFragment settings = new SettingsDialogFragment();
        Bundle args = new Bundle();
        args.putBoolean(EXTRA, isExtra);
        args.putBoolean(HOURLY, isHourly);
        settings.setArguments(args);
        settings.show(getSupportFragmentManager(), SETTINGS_DIALOG);
    }
    private void startLastCitiesActivity() {
        if (Weather.getWeatherList().size() > 0){
            Intent intent = new Intent(MainActivity.this, LastCitiesActivity.class);
            startActivityForResult(intent, SELECT_CITY_REQUEST_CODE);
        } else {
            Toast.makeText(this, getResources().getString(R.string.list_empty), Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onActivityReenter(int resultCode, Intent intent) {
        super.onActivityReenter(resultCode, intent);
        if (resultCode == ENTER_CITY_RESULT_CODE){
            String city = intent.getStringExtra(CITY);
            if (city != null) {
                setWeather(intent.getStringExtra(CITY));
            }
            isExtra = intent.getBooleanExtra(EXTRA, true);
            isHourly = intent.getBooleanExtra(HOURLY, true);
            preference.saveSettings(isExtra, isHourly);
            setOtherFragments();
            return;
        }
        if (resultCode == SETTINGS_RESULT_CODE){
            isExtra = intent.getBooleanExtra(EXTRA, isExtra);
            isHourly = intent.getBooleanExtra(HOURLY, isHourly);
            preference.saveSettings(isExtra, isHourly);
            setOtherFragments();
            setWeather(preference.getSavedCity());
        }
        if (resultCode == BACKGROUND_RESULT_CODE){
            int b = intent.getIntExtra(BACKGROUND,0);
            setBackground(b);
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_CITY_REQUEST_CODE && resultCode == SELECT_CITY_RESULT_CODE) {
            if (data != null){
                setWeather(data.getStringExtra(CITY));
            }
        }
    }
    //паттерн наблюдатель
    @Override
    public void subscribe(Observer observer) {
        ownFragments.add(observer);
    }
    @Override
    public void unsubscribe(Observer observer) {
        ownFragments.remove(observer);
    }
    @Override
    public void notifyObserver() {
        preference.saveCity(Weather.getCity());
        for (Observer observer: ownFragments) {
            if (observer instanceof HourlyWeatherFragment) {
                observer.updateWeather(Weather.getHourlyForecast());
            } else {
                observer.updateWeather(Weather.getCurrentWeather());
            }
        }
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.dlt_main);
        if (drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Weather.setListeners(null);
        unbindService(connection);
        unregisterReceiver(batteryReceiver);
//        RefWatcher refWatcher = WeatherApp.getRefWatcher(this);
//        refWatcher.watch(this);
    }
}