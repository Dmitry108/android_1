package ru.bdim.weather.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.Locale;

import ru.bdim.weather.R;
import ru.bdim.weather.addiyional.Constants;
import ru.bdim.weather.addiyional.Weather;

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
                Weather weather = (Weather) getArguments().getParcelable(WEATHER);
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
    }
}