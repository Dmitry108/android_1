package ru.bdim.weather;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView city = findViewById(R.id.city);
        city.setText(getResources().getString(R.string.mow));
        int cl = 1;
        TextView clouds = findViewById(R.id.clouds);
        clouds.setText(getResources().getString(cl==0 ? R.string.clear:
                cl==1 ? R.string.cloudly : R.string.rain));
        ImageView cloudsImage = findViewById(R.id.imageView);
        cloudsImage.setImageResource(cl==0 ? R.drawable.sun:
                cl==1 ? R.drawable.cloud : R.drawable.rain);

        TextView t = findViewById(R.id.temperature);
        int tt = 20;
        t.setTextColor(getResources().getColor(tt<0 ? R.color.colorCold:
                tt<27 ? R.color.colorWarm : R.color.colorVeryWarm));
        t.setText(String.format("+%dC",tt));
    }
}
