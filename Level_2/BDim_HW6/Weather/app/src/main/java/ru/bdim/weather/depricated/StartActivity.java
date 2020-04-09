package ru.bdim.weather.depricated;

import androidx.appcompat.app.AppCompatActivity;

import ru.bdim.weather.addiyional.Constants;

public class StartActivity extends AppCompatActivity implements Constants {
//    private Parcel parcel;
//    private SettingsPreference preference;
//    private CheckBox cbxAdditional;
//    private CheckBox cbxSunMoon;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_start);
//
//        preference = new SettingsPreference(this);
//
//        AutoCompleteTextView actCity = findViewById(R.id.tv_choice_city);
//        actCity.setText(preference.getSavedCity());
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item,
//                getResources().getStringArray(R.array.pop_cities));
//        actCity.setAdapter(adapter);
//
//        cbxAdditional = findViewById(R.id.cbx_extra_parameters);
//        cbxSunMoon = findViewById(R.id.cbx_sun_moon);
//        //cbxAdditional.setChecked(preference.getSavedExtra());
//        //cbxSunMoon.setChecked(preference.getSavedSun());
//
//        actCity.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getBaseContext(), "'M%'",Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        Button btnOk = findViewById(R.id.btn_ok);
//        btnOk.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                AutoCompleteTextView actCity = findViewById(R.id.tv_choice_city);
//                final String city = actCity.getText().toString();
//                CurrentWeather.ConnectionListener listener = new CurrentWeather.ConnectionListener() {
//                    @Override
//                    public void settingComplete() {
//                        start(parcel);
//                    }
//                    @Override
//                    public void errorConnection() {
//                        parcel = null;
//                        Toast.makeText(StartActivity.this, getResources().getString(R.string.error_connection), Toast.LENGTH_SHORT).show();
//                    }
//                };
//                if (parcel == null || !parcel.getCity().equals(city)) {
//                    parcel = new Parcel(city, getResources().getString(R.string.lang), listener);
//                } else {
//                    start(parcel);
//                }
//            }
//        });
//    }
//    private void start(Parcel parcel){
//        LastCitiesPresenter.getInstance().addCityToList(parcel.getWeather());
//        Intent intent = new Intent(StartActivity.this, MainActivity.class);
//        parcel.setParameters(cbxAdditional.isChecked(), cbxSunMoon.isChecked());
//        intent.putExtra(CITY, parcel);
//        startActivity(intent);
//    }
//    @Override
//    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
//        super.onCreateContextMenu(menu, v, menuInfo);
//        getMenuInflater().inflate(R.menu.menu_context_last_citirs_list, menu);
//    }
//    @Override
//    public boolean onContextItemSelected(MenuItem item) {
//        RecyclerView recyclerView = findViewById(R.id.rcv_last_cities);
//        RecyclerAdapter adapter = (RecyclerAdapter) recyclerView.getAdapter();
//        if (adapter != null) {
//            switch (item.getItemId()) {
//                case R.id.mct_open:
//                    adapter.getSelectedItem().callOnClick();
//                    return true;
//                case R.id.mct_delete:
//                    adapter.removeItem();
//                    return true;
//            }
//        }
//        return super.onContextItemSelected(item);
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        //preference.saveStatus(parcel.getCity(), parcel.isExtra(), parcel.isSunAndMoon());
//        parcel = null;
//        preference = null;
//    }
}