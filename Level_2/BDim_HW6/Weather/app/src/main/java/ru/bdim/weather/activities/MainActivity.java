package ru.bdim.weather.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.net.ConnectivityManager;
import android.net.Network;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

import ru.bdim.weather.R;
import ru.bdim.weather.addiyional.Constants;
import ru.bdim.weather.application.BatteryReceiver;
import ru.bdim.weather.data.InternetWeather;
import ru.bdim.weather.data.SettingsPreference;
import ru.bdim.weather.data.Weather;
import ru.bdim.weather.dialogs.EnterCityDialogFragment;
import ru.bdim.weather.fragments.ExtraParametersFragment;
import ru.bdim.weather.fragments.HourlyWeatherFragment;
import ru.bdim.weather.fragments.MainWeatherFragment;
import ru.bdim.weather.patterns.Observer;
import ru.bdim.weather.patterns.Publish;

public class MainActivity extends AppCompatActivity implements Constants, Publish {
    private List<Observer> ownFragments = new ArrayList<>();
    private boolean isExtra = true;
    private boolean isHourly = true;
    private boolean isConnected = false;
    private SettingsPreference preference;
    private ServiceConnection connection;

    //Options Menu
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
                Toast.makeText(getBaseContext(),getResources().getString(R.string.settings),Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onContextItemSelected(item);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_main);

        //Обработка бокового меню
        Toolbar toolbar = findViewById(R.id.tlb_main);
        setSupportActionBar(toolbar);

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
                    case R.id.mdw_last_cities:
                        startLastCitiesActivity();
                        break;
                }
                DrawerLayout drawer = findViewById(R.id.dlt_main);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });
        //фон
        //добавить возможность выбора картинки в зависимости от времени года и суток
        ScrollView layout = findViewById(R.id.lyt_main_activity);
        layout.setBackgroundResource(R.drawable.sprint);

        preference = new SettingsPreference(this);
        final String city = preference.getSavedCity();

        //отслеживание состяния сети
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            ConnectivityManager.NetworkCallback net = new ConnectivityManager.NetworkCallback(){
                @Override
                public void onAvailable(@NonNull Network network) {
                    super.onAvailable(network);
                    isConnected = true;
                }
                @Override
                public void onLost(@NonNull Network network) {
                    super.onLost(network);
                    Toast.makeText(getBaseContext(), getResources().getString(R.string.connection_lost), Toast.LENGTH_SHORT).show();
                    isConnected = false;
                }
            };
            cm.registerDefaultNetworkCallback(net);
        }
        BatteryReceiver batteryReceiver = new BatteryReceiver();
        registerReceiver(batteryReceiver, new IntentFilter("android.intent.action.BATTERY_LOW"));

        //initNotificationChannel();

        MainWeatherFragment mainWeatherFragment = (MainWeatherFragment) getSupportFragmentManager().findFragmentById(R.id.fmt_main_weather);
        if (mainWeatherFragment != null) {
            subscribe(mainWeatherFragment);
        }
        if (isExtra) {
            addExtraParametersFragment();
        }
        if (isHourly) {
            addHourlyWeatherFragment();
        }
        initData(city);
    }
    private void initData(final String city){
        Weather.init();
        //привязывание к сервису для работы с интернетом
        Intent intent = new Intent(MainActivity.this, InternetWeather.class);
        connection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                Weather.setBinder((InternetWeather.ServiceBinder)service);
                if (city == null || city.equals("")) {
                    showEnterCityDialogFragment();
                } else {
                    setWeather(city);
                }
            }
            @Override
            public void onServiceDisconnected(ComponentName name) {
                Weather.setBinder(null);
            }
        };
        bindService(intent, connection, BIND_AUTO_CREATE);
    }
    private void setWeather(final String city) {
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
        if (isConnected){
            Weather.refresh(city, getResources().getString(R.string.lang), listener);
        } else {
            //Если нет соединения загружать из базы данных
            Toast.makeText(this, getResources().getString(R.string.connection_abs), Toast.LENGTH_SHORT).show();
            Weather.loadLast(city, listener);
        }
    }
    private void initNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel channel = new NotificationChannel("2", "name", importance);
            notificationManager.createNotificationChannel(channel);
        }
    }

//продумать сокращение одинакового кода
//    private void addFragments(Class<? extends Fragment> FragmentClass) {
//    }
    private void addExtraParametersFragment() {
        ExtraParametersFragment extraParametersFragment = new ExtraParametersFragment();
        subscribe(extraParametersFragment);
        FragmentTransaction fTrans = getSupportFragmentManager().beginTransaction();
        fTrans.replace(R.id.flt_extra_parameters, extraParametersFragment);
        fTrans.commit();
    }
    private void addHourlyWeatherFragment() {
        HourlyWeatherFragment hourlyWeatherFragment = new HourlyWeatherFragment();
        subscribe(hourlyWeatherFragment);
        FragmentTransaction fTrans = getSupportFragmentManager().beginTransaction();
        fTrans.replace(R.id.flt_hourly_weather, hourlyWeatherFragment);
        fTrans.commit();
    }
//    private void removeExtraParametersFragment() {
//        ExtraParametersFragment extraParametersFragment = (ExtraParametersFragment)
//                getSupportFragmentManager().findFragmentById(R.id.frm_extra);
//        if (extraParametersFragment != null) {
//            unsubscribe(extraParametersFragment);
//            FragmentTransaction fTrans = getSupportFragmentManager().beginTransaction();
//            fTrans.remove(extraParametersFragment);
//            fTrans.commit();
//        }
//    }
    private void showEnterCityDialogFragment() {
        Toast.makeText(getBaseContext(),getResources().getString(R.string.select_city), Toast.LENGTH_SHORT).show();
        EnterCityDialogFragment enterCity = new EnterCityDialogFragment();
        enterCity.show(getSupportFragmentManager(), ENTER_CITY_DIALOG);
    }
    private void startLastCitiesActivity() {
        if (Weather.getWeatherList().size() > 0){
            Intent intent = new Intent(MainActivity.this, LastCitiesActivity.class);
            startActivityForResult(intent, SELECT_CITY_REQUEST_CODE);
        } else {
            //отобразить, что список пуст
        }
    }
    @Override
    public void onActivityReenter(int resultCode, Intent city) {
        super.onActivityReenter(resultCode, city);
        if (resultCode == ENTER_CITY_RESULT_CODE){
            setWeather(city.getStringExtra(CITY));
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
        preference.saveStatus(Weather.getCity());
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
        unbindService(connection);
    }
}