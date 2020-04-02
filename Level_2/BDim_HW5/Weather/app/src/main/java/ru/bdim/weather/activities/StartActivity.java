package ru.bdim.weather.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import ru.bdim.weather.R;
import ru.bdim.weather.addiyional.Constants;
import ru.bdim.weather.addiyional.LastCitiesPresenter;
import ru.bdim.weather.addiyional.Parcel;
import ru.bdim.weather.addiyional.LastSettingsPreference;
import ru.bdim.weather.addiyional.RecyclerAdapter;
import ru.bdim.weather.addiyional.Weather;

public class StartActivity extends AppCompatActivity implements Constants {
    private Parcel parcel;
    private LastCitiesPresenter presenter;
    private LastSettingsPreference preference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        presenter = LastCitiesPresenter.getInstance();
        preference = new LastSettingsPreference(this);

        AutoCompleteTextView actCity = findViewById(R.id.tv_choice_city);
        actCity.setText(preference.getSavedCity());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item,
                getResources().getStringArray(R.array.pop_cities));
        actCity.setAdapter(adapter);

        final CheckBox cbxAdditional = findViewById(R.id.cbx_extra_parameters);
        final CheckBox cbxSunMoon = findViewById(R.id.cbx_sun_moon);
        cbxAdditional.setChecked(preference.getSavedExtra());
        cbxSunMoon.setChecked(preference.getSavedSun());

        Button btnOk = findViewById(R.id.btn_ok);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AutoCompleteTextView actCity = findViewById(R.id.tv_choice_city);
                final String city = actCity.getText().toString();
                Weather.ConnectionListener listener = new Weather.ConnectionListener() {
                    @Override
                    public void settingComplete() {
                        start(parcel);
                    }
                    @Override
                    public void errorConnection() {
                        parcel = null;
                        Toast.makeText(StartActivity.this, getResources().getString(R.string.error_connection), Toast.LENGTH_SHORT).show();
                    }
                };
                if (parcel == null || !parcel.getCity().equals(city)) {
                    parcel = new Parcel(city, getResources().getString(R.string.lang), listener);
                    parcel.setParameters(cbxAdditional.isChecked(), cbxSunMoon.isChecked());
                } else
                    start(parcel);
            }
        });
    }

    private void start(Parcel parcel){
        presenter.addCityToList(parcel.getWeather());
        Intent intent = new Intent(StartActivity.this, MainActivity.class);
        intent.putExtra(CITY, parcel);
        startActivity(intent);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.menu_context_last_citirs_list, menu);
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        RecyclerView recyclerView = findViewById(R.id.rcv_last_cities);
        RecyclerAdapter adapter = (RecyclerAdapter) recyclerView.getAdapter();
        if (adapter != null) {
            switch (item.getItemId()) {
                case R.id.mct_open:
                    adapter.getSelectedItem().callOnClick();
                    return true;
                case R.id.mct_delete:
                    adapter.removeItem();
                    return true;
            }
        }
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        preference.saveStatus(parcel.getCity(), parcel.isExtra(), parcel.isSunAndMoon());
        parcel = null;
        presenter = null;
        preference = null;
    }
}