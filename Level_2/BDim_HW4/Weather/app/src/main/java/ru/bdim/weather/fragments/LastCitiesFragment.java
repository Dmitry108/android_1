package ru.bdim.weather.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ru.bdim.weather.R;
import ru.bdim.weather.addiyional.RecyclerAdapter;
import ru.bdim.weather.addiyional.Weather;

public class LastCitiesFragment extends Fragment {
    //Вернуться сюда когда изучу передачу данных между фрагментами
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        super.onCreateView(inflater, container, savedInstanceState);
//        return inflater.inflate(R.layout.fragment_last_cities, container, false);
//    }
//    @Override
//    public void onActivityCreated(Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        View view = getView();
//        RecyclerView rcvLastCity = view.findViewById(R.id.rcv_last_cities);
//        rcvLastCity.setHasFixedSize(true);
//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext());
//        rcvLastCity.setLayoutManager(layoutManager);
//
//        List<Weather> lastCitiesList = new ArrayList<>();
//        lastCitiesList.add(new Weather("Moscow"));
//        lastCitiesList.add(new Weather("London"));
//        RecyclerAdapter adapter = new RecyclerAdapter(lastCitiesList);
//        rcvLastCity.setAdapter(adapter);
//        RecyclerAdapter.OnItemClickListener clickListener = new RecyclerAdapter.OnItemClickListener() {
//        @Override
//        public void onClickListener(View view, int position) {
//            AutoCompleteTextView act = view.findViewById(R.id.tv_choice_city);
//            TextView tvw = view.findViewById(R.id.tvw_last_city);
//            Button btn = view.findViewById(R.id.btn_ok);
//            act.setText(tvw.getText());
//            parcel.setWeather(lastCitiesList.get(position));
//            btn.callOnClick();
//        }
//    };
//        adapter.setClickListener(clickListener);
//
//    private void addCityToList(Weather weather) {
//        for (Weather w : lastCitiesList) {
//            if (w.getCity().equals(weather.getCity())) {
//                lastCitiesList.remove(w);
//                break;
//            }
//        }
//        lastCitiesList.add(0, weather);
//        Toast.makeText(this,"добавили", Toast.LENGTH_SHORT).show();
//    }

}
