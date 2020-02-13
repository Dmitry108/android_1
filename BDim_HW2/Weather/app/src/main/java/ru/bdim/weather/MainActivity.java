package ru.bdim.weather;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choise_settings);

        final EditText city = findViewById(R.id.choisedCity);
        ListView citiesList = findViewById(R.id.citiesList);
        citiesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                city.setText((String) parent.getItemAtPosition(position));
            }
        });
        Button btn = findViewById(R.id.ok);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.activity_main);
                weather(city.getText().toString());
            }
        });
        }

    private void weather(String city) {
        ImageView back = findViewById(R.id.back);
        back.setImageResource(R.drawable.winter);

        TextView date = findViewById(R.id.timedate);
        DateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
        date.setText(dateFormat.format(new Date()));

        TextView cityTw = findViewById(R.id.city);
        cityTw.setText(city);//getResources().getString(R.string.mow));

        int cl = 1;
        TextView clouds = findViewById(R.id.clouds);
        clouds.setText(getResources().getString(cl==0 ? R.string.clear:
                cl==1 ? R.string.cloudly : R.string.rain));
        ImageView cloudsImage = findViewById(R.id.imageView);
        cloudsImage.setImageResource(cl==0 ? R.drawable.sun:
                cl==1 ? R.drawable.cloud : R.drawable.rain);

        TextView t = findViewById(R.id.temperature);
        int tt = -5;
        t.setTextColor(getRGB(tt, -30, 30));
//        (getResources().getColor(tt<0 ? R.color.colorCold:
//                tt<27 ? R.color.colorWarm : R.color.colorVeryWarm));
        t.setText(String.format("%s%dC", tt>0?"+":"", tt));
    }

    // метод определяет цвет в спектре от синего до красного
    // в зависимости от значения х (в данном случае температура)
    // в диапазоне значений от t0 до t путем решения системы линейных уравнений
    public static int getRGB (int x, int t0, int t){
        int r, g, b;
        int dt = (t-t0);
        if (x<t0){
            r = g = 0; b = 255;
        } else if (x < t0+dt/4) {
            r = 0; g = 1024*(x-t0)/dt; b=255;
        } else if (x < t0 + dt/2) {
            r = 0; g =255; b=-1024/dt*(x-t0-dt/2);
        } else if (x< t0 +3*dt/4) {
            r=1024*(x-t0-dt/2)/dt; g= 255; b=0;
        } else if (x < t) {
            r=255; g=-1024/dt*(x-t); b=0;
        } else {
            r=255; g = b =0;
        }
        return Color.rgb(r, g, b);
    }
}