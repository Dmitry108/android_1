package ru.bdim.weather2.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ScrollView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import ru.bdim.weather2.additional.Constants;
import ru.bdim.weather2.additional.Parcel;
import ru.bdim.weather2.additional.Weather;
import ru.bdim.weather2.fragments.ExtraParametersFragment;
import ru.bdim.weather2.R;

public class MainActivity extends AppCompatActivity implements Constants {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Parcel parcel = (Parcel) getIntent().getSerializableExtra(CITY);
        if (parcel != null) {
                        Weather weather = parcel.getWeather();
            Bundle args = new Bundle();
            args.putSerializable(WEATHER, weather);

            Fragment mainWeatherFragment = getSupportFragmentManager().findFragmentById(R.id.fmt_main_weather);
            if (mainWeatherFragment != null) {
                //фон
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