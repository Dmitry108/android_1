package ru.bdim.weather;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ChoiseActivity extends AppCompatActivity {
    private final String TAG = getClass().getName();
    private AutoCompleteTextView citiesList;
    //private final MainPresenter presenter = MainPresenter.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choise_settings);

        citiesList = findViewById(R.id.citiesList);
        citiesList.setAdapter(new ArrayAdapter<>(this,
            R.layout.support_simple_spinner_dropdown_item,
            getResources().getStringArray(R.array.cities)));

        Button btn = findViewById(R.id.ok);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //savePresenter();
                Intent intent = new Intent(ChoiseActivity.this, MainActivity.class);
                intent.putExtra("City", citiesList.getText().toString());
                intent.putExtra("Settings", getSettings());
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
        CheckBox c = findViewById(R.id.temperature);
        settings[0] = c.isChecked();
        c = findViewById(R.id.cloudiness);
        settings[1] = c.isChecked();
        c = findViewById(R.id.wind);
        settings[2] = c.isChecked();
        c = findViewById(R.id.pressure);
        settings[3] = c.isChecked();
        c = findViewById(R.id.humidity);
        settings[4] = c.isChecked();
        return settings;
    }
//    public void savePresenter(){
//        presenter.setCity(citiesList.getText().toString());
//        CheckBox c = findViewById(R.id.temperature);
//        presenter.setIsTemperature(c.isChecked());
//        c = findViewById(R.id.cloudiness);
//        presenter.setIsCloudiness(c.isChecked());
//        c = findViewById(R.id.wind);
//        presenter.setIsWind(c.isChecked());
//        c = findViewById(R.id.pressure);
//        presenter.setIsPressure(c.isChecked());
//        c = findViewById(R.id.humidity);
//        presenter.setIsHumidity(c.isChecked());
//    }
//    public void read(){
//        citiesList.setText(presenter.getCity());
//        CheckBox c = findViewById(R.id.temperature);
//        c.setChecked(presenter.isTemperature());
//        c = findViewById(R.id.cloudiness);
//        c.setChecked(presenter.isCloudiness());
//        c = findViewById(R.id.wind);
//        c.setChecked(presenter.isWind());
//        c = findViewById(R.id.pressure);
//        c.setChecked(presenter.isPressure());
//        c = findViewById(R.id.humidity);
//        c.setChecked(presenter.isHumidity());
//    }
}