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
    private final MainPresenter presenter = MainPresenter.getInstance();

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
                save();
                Intent intent = new Intent(ChoiseActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        String instanceState;
        instanceState = savedInstanceState == null? "Первый запуск!":"Повторный запуск!";
        Toast.makeText(this,instanceState + " - onCreate()",Toast.LENGTH_SHORT).show();
        Log.d(TAG,instanceState + " - onCreate()");
    }
    @Override
    protected void onStart() {
        super.onStart();
        Toast.makeText(this, "onStart()", Toast.LENGTH_SHORT).show();
        Log.d(TAG,"onStart()");
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
        Toast.makeText(this,"Повторный запуск!! - onRestoreInstanceState()",Toast.LENGTH_SHORT).show();
        Log.d(TAG,"Повторный запуск!! - onRestoreInstanceState()");
    }
    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(this,"onResume()",Toast.LENGTH_SHORT).show();
        Log.d(TAG,"onResume()");
    }
    @Override
    protected void onPause(){
        super.onPause();
        Toast.makeText(getApplicationContext(), "onPause()", Toast.LENGTH_SHORT).show();
        Log.d(TAG,"onPause()");
    }
    @Override
    protected void onSaveInstanceState(Bundle saveInstanceState){
        super.onSaveInstanceState(saveInstanceState);

        //saveInstanceState.putString("City",citiesList.getText().toString());
        Toast.makeText(this,"onSavedInstatceState()", Toast.LENGTH_SHORT).show();
        Log.d(TAG,"onSavedInstatceState()");
    }
    @Override
    protected void onStop(){
        super.onStop();
        Toast.makeText(this, "onStop()", Toast.LENGTH_SHORT).show();
        Log.d(TAG,"onStop()");
    }
    @Override
    protected void onRestart(){
        super.onRestart();
        Toast.makeText(this, "onRestart()", Toast.LENGTH_SHORT).show();
        Log.d(TAG,"onRestart()");
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "onDestroy()", Toast.LENGTH_SHORT).show();
        Log.d(TAG,"onDestroy()");
    }
    public void save(){
        presenter.setCity(citiesList.getText().toString());
        CheckBox c = findViewById(R.id.temperature);
        presenter.setIsTemperature(c.isChecked());
        c = findViewById(R.id.cloudiness);
        presenter.setIsCloudiness(c.isChecked());
        c = findViewById(R.id.wind);
        presenter.setIsWind(c.isChecked());
        c = findViewById(R.id.pressure);
        presenter.setIsPressure(c.isChecked());
        c = findViewById(R.id.humidity);
        presenter.setIsHumidity(c.isChecked());
    }
    public void read(){
        citiesList.setText(presenter.getCity());
        CheckBox c = findViewById(R.id.temperature);
        c.setChecked(presenter.isTemperature());
        c = findViewById(R.id.cloudiness);
        c.setChecked(presenter.isCloudiness());
        c = findViewById(R.id.wind);
        c.setChecked(presenter.isWind());
        c = findViewById(R.id.pressure);
        c.setChecked(presenter.isPressure());
        c = findViewById(R.id.humidity);
        c.setChecked(presenter.isHumidity());
    }
}