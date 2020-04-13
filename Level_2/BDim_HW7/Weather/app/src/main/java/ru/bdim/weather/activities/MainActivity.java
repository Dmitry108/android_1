package ru.bdim.weather.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
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
    private boolean isLocation;

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
        isLocation = preference.getSavedLocation();
    }
    private void initData(){
        Log.d(TAG, "инициализация данных");
        //настройка слушателя для работы с интернетом
        Weather.DataListener listener = new Weather.DataListener(){
            @Override
            public void receivingSuccess() {
                notifyObserver();
            }
            @Override
            public void receivingError() {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setIcon(R.drawable.compass)
                        .setMessage(R.string.data_error)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        };
        Weather.setListener(listener);
        //привязывание к сервису для работы с интернетом
        Intent intent = new Intent(MainActivity.this, InternetWeather.class);
        connection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                Weather.setBinder((InternetWeather.ServiceBinder)service);
                setWeather(null, -1, -1);
            }
            @Override
            public void onServiceDisconnected(ComponentName name) {
                Weather.setBinder(null);
            }
        };
        bindService(intent, connection, BIND_AUTO_CREATE);
    }

    private void setWeather(String param, double lat, double lon) {
        if (isConnected) {
            //интернет есть
            if (param == null) {
                if (isLocation) {
                    //в настройках приложения определение геопозиции по умолчанию
                    if (checkNeedPermission()) {
                        //доступ к геолокации есть
                        refresh(null, lat, lon);
                    } else {
                        //нет доступа к геолокации
                        requestNeedPermission();
                    }
                } else {
                    //в настройках приложения без геопозиции
                    setCityFromPref();
                }
            } else {
                //запрос по выбранному городу
                refresh(param, -1, -1);
            }
        } else {
            //интернета нет запросить из базы данных по последнему городу
            Toast.makeText(this, getResources().getString(R.string.connection_abs), Toast.LENGTH_SHORT).show();
            Weather.loadLast(preference.getSavedCity());
        }
    }
    private boolean checkNeedPermission() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED ||
               ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED;
    }
    private void requestNeedPermission() {
        if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE)){
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSION_REQUEST_CODE);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE){
            if (permissions.length == 2 && (
                    grantResults[0] == PackageManager.PERMISSION_GRANTED ||
                    grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
                refresh(null, -1, -1);
            } else {
                setCityFromPref();
            }
        }
    }
    private void setCityFromPref() {
        String city = preference.getSavedCity();
        if (city != null){
            refresh(city, -1, -1);
        } else {
            showEnterCityDialogFragment();
        }
    }
    private void refresh(String city, double lat, double lon) {
        Weather.refresh(this, city, lat, lon);
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
                    case R.id.mdw_use_location:
                        setWeather(COORD, -1, -1);
                        break;
                    case R.id.mdw_open_map:
                        startMapActivity();
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
            case R.id.mob_use_location:
                setWeather(null, -1, -1);
                return true;
            case R.id.mob_open_map:
                startMapActivity();
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
                        }//net);ConnectivityManager.NetworkCallback net =
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
        args.putBoolean(LOCATION, isLocation);
        settings.setArguments(args);
        settings.show(getSupportFragmentManager(), SETTINGS_DIALOG);
    }
    private void startLastCitiesActivity() {
        if (Weather.getWeatherList().size() > 0){
            Intent intent = new Intent(MainActivity.this, LastCitiesActivity.class);
            startActivityForResult(intent, SELECT_CITY_REQUEST_CODE);
        } else {
            //отобразить, что список пуст
        }
    }
    private void startMapActivity() {
        Intent intent = new Intent(MainActivity.this, MapsActivity.class);
        intent.putExtra(LAT, Weather.getCurrentWeather().getLat());
        intent.putExtra(LON, Weather.getCurrentWeather().getLon());
        startActivityForResult(intent, MAP_REQUEST_CODE);
    }
    @Override
    public void onActivityReenter(int resultCode, Intent intent) {
        super.onActivityReenter(resultCode, intent);
        if (resultCode == ENTER_CITY_RESULT_CODE){
            String city = intent.getStringExtra(CITY);
            if (city != null) {
                if (city.equals(COORD)) {
                    isLocation = true;
                    setWeather(null, -1, -1);
                } else {
                    setWeather(intent.getStringExtra(CITY), -1, -1);
                }
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
            isLocation = intent.getBooleanExtra(LOCATION, isLocation);
            preference.saveSettings(isExtra, isHourly);
            preference.saveLocation(isLocation);
            setOtherFragments();
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
                setWeather(data.getStringExtra(CITY), -1, -1);
            }
        }
        if (requestCode == MAP_REQUEST_CODE && resultCode == MAP_RESULT_CODE){
            if (data != null){
                double lat = data.getDoubleExtra(LAT, -1);
                double lon = data.getDoubleExtra(LON, -1);
                if (lat >=0 && lon >=0){
                    setWeather(null, lat, lon);
                }
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
        Weather.setListener(null);
        unbindService(connection);
        unregisterReceiver(batteryReceiver);
        RefWatcher refWatcher = WeatherApp.getRefWatcher(this);
        refWatcher.watch(this);
    }
}