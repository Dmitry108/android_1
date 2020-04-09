package ru.bdim.weather.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import ru.bdim.weather.R;
import ru.bdim.weather.addiyional.HourlyWeatherAdapter;
import ru.bdim.weather.data.CurrentWeather;
import ru.bdim.weather.data.Data;
import ru.bdim.weather.data.HourlyForecast;
import ru.bdim.weather.patterns.Observer;
import ru.bdim.weather.views.GraphicView;

public class HourlyWeatherFragment extends Fragment implements Observer {
    private HourlyForecast forecast;
    //private boolean visibility;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_hourly_weather, container, false);
//        view.setVisibility(View.GONE);
//        visibility = false;
        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        View view = getView();
        if (view != null && forecast != null) {
            RecyclerView hourlyRecycler = view.findViewById(R.id.rcv_hourly_weather);
            hourlyRecycler.setHasFixedSize(true);
            HourlyWeatherAdapter adapter = new HourlyWeatherAdapter(forecast.getHourlyWeatherList());
            hourlyRecycler.setAdapter(adapter);
            GraphicView grv = view.findViewById(R.id.cvw_hourly_graphic);
            grv.setChartData(forecast.getTempArray());
        }
    }
    @Override
    public void updateWeather(Data forecast) {

        this.forecast = (HourlyForecast) forecast;
//        View view = getView();
        //if (view == null) onCreate(null);
//        if (view != null && !visibility) {
//            view.setVisibility(View.VISIBLE);
//        }
        onResume();
    }
}