package ru.bdim.weather.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;


import ru.bdim.weather.R;
import ru.bdim.weather.addiyional.Constants;
import ru.bdim.weather.addiyional.Parcel;
import ru.bdim.weather.addiyional.Weather;
import ru.bdim.weather.fragments.ExtraParametersFragment;
import ru.bdim.weather.fragments.MainWeatherFragment;
import ru.bdim.weather.patterns.Observer;
import ru.bdim.weather.patterns.Publish;

public class MainActivity extends AppCompatActivity implements Constants, Publish {
    private List<Observer> ownFragments = new ArrayList<>();
    //Options Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_option_main, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.mob_select_city:
                startSelectCityActivity();
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
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer,  toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigator = findViewById(R.id.ngv_main);
        navigator.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.mdw_select_city:
                        startSelectCityActivity();
                        break;
                }
                DrawerLayout drawer = findViewById(R.id.dlt_main);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        Parcel parcel = (Parcel) getIntent().getParcelableExtra(CITY);
        if (parcel != null) {
            Weather weather = parcel.getWeather();
            Bundle args = new Bundle();
            args.putParcelable(WEATHER, weather);

            MainWeatherFragment mainWeatherFragment = (MainWeatherFragment) getSupportFragmentManager().findFragmentById(R.id.fmt_main_weather);
            if (mainWeatherFragment != null) {
                subscribe(mainWeatherFragment);
                //фон
                //добавить возможность выбора картинки в зависимости от времени года и суток
                ScrollView layout = findViewById(R.id.lyt_main_activity);
                layout.setBackgroundResource(R.drawable.sprint);

                mainWeatherFragment.setArguments(args);
            }
            if (parcel.isExtra()){
                ExtraParametersFragment extraParametersFragment = new ExtraParametersFragment();
                subscribe(extraParametersFragment);
                FragmentTransaction fTrans = getSupportFragmentManager().beginTransaction();
                extraParametersFragment.setArguments(args);
                fTrans.replace(R.id.flt_extra_parameters, extraParametersFragment);
                fTrans.commit();
            }
        }
    }

    private void startSelectCityActivity() {
        Intent intent = new Intent(MainActivity.this, SelectCityActivity.class);
        startActivityForResult(intent, SELECT_CITY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_CITY_REQUEST_CODE && resultCode == SELECT_CITY_RESULT_CODE) {
            if (data != null){
                Weather weather = data.getParcelableExtra(WEATHER);
                //через наблюдатель передать данные в mainfragment и extrafragment
                notifyObserver(weather);
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
    public void notifyObserver(Weather weather) {
        for (Observer observer: ownFragments) {
            observer.updateWeather(weather);
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
}