package ru.bdim.weather2.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.Locale;

import ru.bdim.weather2.R;
import ru.bdim.weather2.additional.Weather;
import ru.bdim.weather2.additional.Constants;

public class ExtraParametersFragment extends Fragment implements Constants {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_extra_parameters, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View view = getView();
        if (view != null) {
            Bundle args = getArguments();
            if (args != null) {
                Weather weather = (Weather) getArguments().getSerializable(WEATHER);
                if (weather != null) {
                    //ветер
                    int v = weather.getWindSpeed();
                    int dir = weather.getWindDir();
                    TextView tvwWind = view.findViewById(R.id.tvw_wind);
                    tvwWind.setText(String.format(Locale.ROOT, "%s %d %s",
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
    }
}