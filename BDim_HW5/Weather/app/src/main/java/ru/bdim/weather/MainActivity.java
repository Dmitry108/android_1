package ru.bdim.weather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final String CITY = "City";
    private static final String SETTINGS = "Settings";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //обработка событий
        View.OnLongClickListener infoClick = new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                TextView someTv = findViewById(v.getId());
                String text = someTv.getText().toString();
                if (v.getId() == R.id.timedate) text = text.substring(0, text.length() - 5);
                String url = String.format("%s%s", getResources().getString(R.string.wiki),
                        text);
                Uri uri = Uri.parse(url);
                Intent browser = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(browser);
                return false;
            }
        };
        //фон
        ConstraintLayout layout = findViewById(R.id.layout);
        layout.setBackgroundResource(R.drawable.winter);
        //дата
        final TextView date = findViewById(R.id.timedate);
        DateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
        date.setText(dateFormat.format(new Date()));
        date.setOnLongClickListener(infoClick);
        //город
        final TextView cityTw = findViewById(R.id.city);
        cityTw.setText(getIntent().getStringExtra(CITY));
        cityTw.setOnLongClickListener(infoClick);

        boolean[] settings = getIntent().getBooleanArrayExtra(SETTINGS);
        FragmentTransaction fTrans;
        fTrans = getSupportFragmentManager().beginTransaction();
        if(settings[0]) {
            CloudinessFragment clFrag = CloudinessFragment.create(1);
            fTrans.add(R.id.cloudiness,clFrag);
        }
        if (settings[1]) {
            Toast.makeText(this, "Температура работает", Toast.LENGTH_SHORT).show();
            TemperatureFragment tFrag = TemperatureFragment.create(-5);
            fTrans.add(R.id.temperature,tFrag);
        }
        if(settings[2]) {
            WindFragment wFrag = WindFragment.create(0,5);
            fTrans.add(R.id.wind,wFrag);
        }
        if(settings[3]) {
            PressureFragment pFrag = PressureFragment.create(760);
            fTrans.add(R.id.pressure, pFrag);
        }
        if(settings[4]) {
            HumidityFragment hFrag = HumidityFragment.create(70);
            fTrans.add(R.id.humidity, hFrag);
        }
        fTrans.commit();
    }
}