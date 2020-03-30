package ru.bdim.weather.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.ScrollView;

import ru.bdim.weather.R;
import ru.bdim.weather.addiyional.Constants;
import ru.bdim.weather.addiyional.Parcel;
import ru.bdim.weather.addiyional.Weather;
import ru.bdim.weather.fragments.ExtraParametersFragment;

public class MainActivity extends AppCompatActivity implements Constants {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Parcel parcel = (Parcel) getIntent().getParcelableExtra(CITY);
        if (parcel != null) {
            Weather weather = parcel.getWeather();
            Bundle args = new Bundle();
            args.putParcelable(WEATHER, weather);

            Fragment mainWeatherFragment = getSupportFragmentManager().findFragmentById(R.id.fmt_main_weather);
            if (mainWeatherFragment != null) {
                //фон
                //добавить возможность выбора картинки в зависимости от времени года и суток
                ScrollView layout = findViewById(R.id.lyt_main_activity);
                layout.setBackgroundResource(R.drawable.sprint);

                mainWeatherFragment.setArguments(args);
            }
            if (parcel.isExtra()){
                ExtraParametersFragment extraParametersFragment = new ExtraParametersFragment();
                FragmentTransaction fTrans = getSupportFragmentManager().beginTransaction();
                extraParametersFragment.setArguments(args);
                fTrans.replace(R.id.flt_extra_parameters, extraParametersFragment);
                fTrans.commit();
            }
        }
    }
}