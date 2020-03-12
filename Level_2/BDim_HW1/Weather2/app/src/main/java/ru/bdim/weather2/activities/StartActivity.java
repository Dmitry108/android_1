package ru.bdim.weather2.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ru.bdim.weather2.additional.Constants;
import ru.bdim.weather2.additional.Parcel;
import ru.bdim.weather2.R;
import ru.bdim.weather2.additional.Constants;
import ru.bdim.weather2.additional.Parcel;
import ru.bdim.weather2.additional.RecyclerAdapter;
import ru.bdim.weather2.additional.Weather;

public class StartActivity extends AppCompatActivity implements Constants {
    private Parcel parcel;
    private List<Weather> lastCities = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        final AutoCompleteTextView atwCity = findViewById(R.id.atw_choice_city);
        atwCity.setAdapter(new ArrayAdapter<>(this,
                    R.layout.support_simple_spinner_dropdown_item,
                    getResources().getStringArray(R.array.cities)));

        Button btnOk = findViewById(R.id.btn_ok);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //EditText etxCity = findViewById(R.id.tv_choice_city);
                String city = atwCity.getText().toString();
                DateFormat dateFormat = new SimpleDateFormat("d MMMM yyyy", Locale.getDefault());
                Date dateToday = new Date();
                String date = dateFormat.format(dateToday);
                if (parcel == null || !parcel.getCity().equals(city)) {
                    parcel = new Parcel(city, date);
                }

                startMainActivity(parcel);

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        initLastCitiesRecycler(lastCities);
    }
    private void startMainActivity(Parcel parcel){
        addCityToList(parcel.getWeather());
        CheckBox cbxAdditional = findViewById(R.id.cbx_extra_parameters);
        CheckBox cbxSunMoon = findViewById(R.id.cbx_sun_moon);
        Intent intent = new Intent(StartActivity.this, MainActivity.class);
        parcel.setParameters(cbxAdditional.isChecked(), cbxSunMoon.isChecked());
        intent.putExtra(CITY, parcel);
        startActivity(intent);
    }

    private void addCityToList(Weather weather){
        for (Weather w: lastCities){
            if (w.getCity().equals(weather.getCity())){
                lastCities.remove(w); break;
            }
        }
        lastCities.add(0, weather);
    }
    private void initLastCitiesRecycler(final List<Weather> data){
        RecyclerView recyclerView = findViewById(R.id.rcv_last_cities);
        final AutoCompleteTextView atwCity = findViewById(R.id.atw_choice_city);
        final Button btnOk = findViewById(R.id.btn_ok);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        RecyclerAdapter adapter = new RecyclerAdapter(data);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                TextView tvwCity = view.findViewById(R.id.tvw_list_city);
                atwCity.setText(tvwCity.getText());
                startMainActivity(new Parcel(data.get(position)));
            }
        });
    }
}