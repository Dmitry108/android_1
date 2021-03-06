package ru.bdim.weather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    //private final MainPresenter presenter = MainPresenter.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //обработка событий
        View.OnLongClickListener infoClick = new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                TextView r = findViewById(v.getId());
                String d = r.getText().toString();
                if (v.getId()==R.id.timedate) d = d.substring(0,d.length()-5);
                String url = String.format("%s%s", getResources().getString(R.string.wiki),
                        d);
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
        cityTw.setText(getIntent().getStringExtra("City"));//presenter.getCity());
        cityTw.setOnLongClickListener(infoClick);

        boolean[] settings = getIntent().getBooleanArrayExtra("Settings");
        int cl = 1;//presenter.getCloudiness();
        TextView clouds = findViewById(R.id.clouds);
        clouds.setText(getResources().getString(cl==0 ? R.string.clear:
                cl==1 ? R.string.cloudly : R.string.rain));
        ImageView cloudsImage = findViewById(R.id.imageView);
        cloudsImage.setImageResource(cl==0 ? R.drawable.sun:
                cl==1 ? R.drawable.cloud : R.drawable.rain);

        TextView t = findViewById(R.id.temperature);
        int tt = 5;// presenter.getTemperature();
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