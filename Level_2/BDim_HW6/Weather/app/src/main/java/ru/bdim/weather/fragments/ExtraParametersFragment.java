package ru.bdim.weather.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.Locale;

import ru.bdim.weather.R;
import ru.bdim.weather.addiyional.Constants;
import ru.bdim.weather.data.CurrentWeather;
import ru.bdim.weather.data.Data;
import ru.bdim.weather.patterns.Observer;

public class ExtraParametersFragment extends Fragment implements Constants, Observer {
    private CurrentWeather weather;
    private boolean visibility;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_extra_parameters, container, false);
//        view.setVisibility(View.INVISIBLE);
//        visibility = false;
        Toast.makeText(container.getContext(), "первый метод", Toast.LENGTH_SHORT).show();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        View view = getView();
        if (view != null) {
            if (weather != null) {
                                //ветер
                float v = weather.getWindSpeed();
                int dir = weather.getWindDir();
                TextView tvwWind = view.findViewById(R.id.tvw_wind);
                tvwWind.setText(String.format(Locale.ROOT, "%s %.1f %s",
                        getResources().getStringArray(R.array.wind_direction)[dir], v,
                        getResources().getString(R.string.m_s)));
                //влажность
                int h = weather.getHumidity();
                TextView tvwHumidity = view.findViewById(R.id.tvw_humidity);
                tvwHumidity.setText(String.format(Locale.ROOT, "%d %%", h));
                //давление
                int p = weather.getPressure();
                TextView tvwPressure = view.findViewById(R.id.tvw_pressure);
                tvwPressure.setText(String.format(Locale.ROOT, "%d %s", p,
                        getResources().getString(R.string.mmHg)));
            }
        }
    }
    @Override
    public void updateWeather(Data weather) {
        this.weather = (CurrentWeather) weather;
//        View view = getView();
//        if (view != null && !visibility) {
//            view.setVisibility(View.VISIBLE);
//            visibility = true;
//        }
        onResume();
    }
}