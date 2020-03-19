package ru.bdim.weather;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
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

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.regex.Pattern;

public class ChoiseActivity extends AppCompatActivity {
    private static final String TAG = "tag";
    private static final String CITY = "City";
    private static final String SETTINGS = "Settings";
    private AutoCompleteTextView citiesList;
    private FloatingActionButton btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice_settings);
        System.out.println("Start activity");
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
                String str = citiesList.getText().toString();

                Intent intent = new Intent(ChoiseActivity.this, MainActivity.class);
                intent.putExtra(CITY, str);
                intent.putExtra(SETTINGS, getSettings());
                startActivity(intent);
            }
        });
    }
    public void showMessage (String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        Log.d(TAG, msg);
    }
    public boolean getSettings(){
        boolean settings = ((CheckBox)findViewById(R.id.cbx_additional_parameters)).isChecked();
        return settings;
    }
    private void initLastCitiesRecycler(String[] data){
        RecyclerView recyclerView = findViewById(R.id.recyclerLastCities);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        RecyclerAdapter adapter = new RecyclerAdapter(data);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                citiesList.setText(((TextView)view).getText());
                btn.callOnClick();
            }
        });
    }
}