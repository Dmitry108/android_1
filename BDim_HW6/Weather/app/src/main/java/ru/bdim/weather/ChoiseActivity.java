package ru.bdim.weather;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ChoiseActivity extends AppCompatActivity {
    private static final String TAG = "tag";
    private static final String CITY = "City";
    private static final String SETTINGS = "Settings";
    private AutoCompleteTextView citiesList;
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choise_settings);

        citiesList = findViewById(R.id.citiesList);
        citiesList.setAdapter(new ArrayAdapter<>(this,
            R.layout.support_simple_spinner_dropdown_item,
            getResources().getStringArray(R.array.cities)));

        String[] data = getResources().getStringArray(R.array.lastCities);
        initLastCitiesRecycler(data);

        btn = findViewById(R.id.ok);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChoiseActivity.this, MainActivity.class);
                intent.putExtra(CITY, citiesList.getText().toString());
                intent.putExtra(SETTINGS, getSettings());
                startActivity(intent);
            }
        });
        String instanceState;
        instanceState = savedInstanceState == null? "Первый запуск!":"Повторный запуск!";
        showMessage(instanceState + " - onCreate()");
    }
    @Override
    protected void onStart() {
        super.onStart();
        showMessage("onStart()");
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
        showMessage("Повторный запуск!! - onRestoreInstanceState()");
    }
    @Override
    protected void onResume() {
        super.onResume();
        showMessage("onResume()");
    }
    @Override
    protected void onPause(){
        super.onPause();
        showMessage("onPause()");
    }
    @Override
    protected void onSaveInstanceState(Bundle saveInstanceState){
        super.onSaveInstanceState(saveInstanceState);
        showMessage("onSavedInstatceState()");
    }
    @Override
    protected void onStop(){
        super.onStop();
        showMessage("onStop()");
    }
    @Override
    protected void onRestart(){
        super.onRestart();
        showMessage("onRestart()");
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        showMessage("onDestroy()");
    }
    public void showMessage (String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        Log.d(TAG, msg);
    }
    public boolean[] getSettings(){
        boolean[] settings = new boolean[5];
        settings[0] = ((CheckBox)findViewById(R.id.cloudiness)).isChecked();
        settings[1] = ((CheckBox)findViewById(R.id.temperature)).isChecked();
        settings[2] = ((CheckBox)findViewById(R.id.wind)).isChecked();
        settings[3] = ((CheckBox)findViewById(R.id.pressure)).isChecked();
        settings[4] = ((CheckBox)findViewById(R.id.humidity)).isChecked();
        return settings;
    }
    private void initLastCitiesRecycler(String[] data){
        RecyclerView recyclerView = findViewById(R.id.recyclerLastCities);
        // Эта установка служит для повышения производительности системы
        recyclerView.setHasFixedSize(true);
        // Будем работать со встроенным менеджером
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        // Установим адаптер
        RecyclerAdapter adapter = new RecyclerAdapter(data);
        recyclerView.setAdapter(adapter);
        // Установим слушателя
        adapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                citiesList.setText(((TextView)view).getText());
                btn.callOnClick();
            }
        });

    }
}