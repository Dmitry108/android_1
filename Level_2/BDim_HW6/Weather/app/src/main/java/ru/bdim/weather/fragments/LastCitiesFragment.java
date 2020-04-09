package ru.bdim.weather.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ru.bdim.weather.R;
import ru.bdim.weather.addiyional.Constants;
import ru.bdim.weather.addiyional.RecyclerAdapter;
import ru.bdim.weather.data.CurrentWeather;
import ru.bdim.weather.data.Weather;

public class LastCitiesFragment extends Fragment implements Constants {
    private List<CurrentWeather> lastCitiesList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_last_cities, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        RecyclerAdapter.OnItemContextMenu contextMenu = new RecyclerAdapter.OnItemContextMenu() {
            @Override
            public void registerContextMenu(View view) {
                registerForContextMenu(view);
            }
        };

        final View view = getView();
        if (view != null) {
            lastCitiesList = Weather.getWeatherList();
            final RecyclerView rcvLastCity = view.findViewById(R.id.rcv_last_cities);
            rcvLastCity.setHasFixedSize(true);
            final RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext());
            rcvLastCity.setLayoutManager(layoutManager);
            RecyclerAdapter adapter = new RecyclerAdapter();
            rcvLastCity.setAdapter(adapter);
            RecyclerAdapter.OnItemClickListener clickListener = new RecyclerAdapter.OnItemClickListener() {
                @Override
                public void onClickListener(View view, int position) {
                    CurrentWeather weather = lastCitiesList.get(position);
                    setResultForActivity(weather.getCity());
                }
            };
            adapter.setItemInterface(clickListener, contextMenu);
        }
    }
    public void setResultForActivity(String city){
        Intent intent = new Intent();
        intent.putExtra(CITY, city);
        Objects.requireNonNull(getActivity()).setResult(SELECT_CITY_RESULT_CODE, intent);
        getActivity().finish();
    }
}