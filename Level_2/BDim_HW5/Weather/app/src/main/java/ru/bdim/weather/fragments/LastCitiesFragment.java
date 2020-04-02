package ru.bdim.weather.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Objects;

import ru.bdim.weather.R;
import ru.bdim.weather.activities.SelectCityActivity;
import ru.bdim.weather.addiyional.Constants;
import ru.bdim.weather.addiyional.LastCitiesPresenter;
import ru.bdim.weather.addiyional.RecyclerAdapter;
import ru.bdim.weather.addiyional.Weather;

public class LastCitiesFragment extends Fragment implements Constants {
    private ArrayList<Weather> lastCitiesList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_last_cities, container, false);
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
             super.onActivityCreated(savedInstanceState);
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
            lastCitiesList = LastCitiesPresenter.getInstance().getWeatherList();
            final RecyclerView rcvLastCity = view.findViewById(R.id.rcv_last_cities);
            rcvLastCity.setHasFixedSize(true);
            final RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext());
            rcvLastCity.setLayoutManager(layoutManager);
            final RecyclerAdapter adapter = new RecyclerAdapter();
            rcvLastCity.setAdapter(adapter);
            RecyclerAdapter.OnItemClickListener clickListener = new RecyclerAdapter.OnItemClickListener() {
                @Override
                public void onClickListener(View view, int position) {
                    Weather weather = lastCitiesList.get(position);

                    //Александр, подскажите! Правильно ли я обращаюсь к методу активити из фрагмента,
                    //или лучше использовать реализацию интерфейса в качестве callback?
                    ((SelectCityActivity) Objects.requireNonNull(
                            getActivity())).backToMainActivity(weather);
                }
            };
            adapter.setItemInterface(clickListener, contextMenu);
        }
    }
}