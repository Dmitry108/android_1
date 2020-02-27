package ru.bdim.weather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
        Date dateToday = new Date();
        date.setText(dateFormat.format(dateToday));
        date.setOnLongClickListener(infoClick);
        //город
        final TextView cityTw = findViewById(R.id.city);
        cityTw.setText(getIntent().getStringExtra(CITY));
        cityTw.setOnLongClickListener(infoClick);
        //recyclerView
        //заполнение тестовыми данными
        //Android Studio не определяет почему-то по добавлении в gradle
        //implementation 'com.android.support:cardview-v7:28.0.0'
        //в ближайшие выходные попытаюсь с этим рабобраться
        //и реализовать список на основе CardView, а также добавить обработку событий,
        //чтоб при нажатии на основном экране отображалась подробная информация по выбранному дню
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM");
        Calendar c = Calendar.getInstance();
        c.setTime(dateToday);
        String[] days = new String[3];
        for  (int i=0; i<3; i++){
            c.add(Calendar.DATE, 1);
            days[i] = sdf.format(c.getTime());
        }
        String[] data = {String.format("%s %s %s", days[0], getResources().getString(R.string.clear), "3C/-4C"),
            String.format("%s %s %s", days[1], getResources().getString(R.string.cloudly), "2C/-6C"),
                String.format("%s %s %s", days[0], getResources().getString(R.string.rain), "5C/-1C")
        };
        initNextDaysRecycler(data);

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
    private void initNextDaysRecycler(String[] data){
        RecyclerView recyclerView = findViewById(R.id.recyclerNextDays);

        // Эта установка служит для повышения производительности системы
        recyclerView.setHasFixedSize(true);

        // Будем работать со встроенным менеджером
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // Установим адаптер
        RecyclerAdapter adapter = new RecyclerAdapter(data);
        recyclerView.setAdapter(adapter);
    }
}