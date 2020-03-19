package ru.bdim.weather;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final String CITY = "City";
    private static final String SETTINGS = "Settings";
    private static final String NameSharedPreference = "THEME";
    private static final String IsDarkTheme = "IS_DARK_THEME";
    private static final SecureRandom rn = new SecureRandom();

    private static int[] imgSkyArray = {
            R.drawable.sun, R.drawable.sun_clouds, R.drawable.clouds,
            R.drawable.rain, R.drawable.lightning, R.drawable.snow};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Resources resources = getResources();
        //обработка событий
        View.OnLongClickListener infoClick = new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                TextView someTv = findViewById(v.getId());
                String text = someTv.getText().toString();
                if (v.getId() == R.id.tvw_date_today) text = text.substring(0, text.length() - 5);
                String url = String.format("%s%s", getResources().getString(R.string.wiki),
                        text);
                Uri uri = Uri.parse(url);
                Intent browser = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(browser);
                return false;
            }
        };
        //фон
        ScrollView layout = findViewById(R.id.lyt_main_activity);
        layout.setBackgroundResource(R.drawable.sprint);
        //дата
        final TextView date = findViewById(R.id.tvw_date_today);
        DateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
        Date dateToday = new Date();
        date.setText(dateFormat.format(dateToday));
        date.setOnLongClickListener(infoClick);
        //город
        final TextView cityTw = findViewById(R.id.tvw_city);
        cityTw.setText(getIntent().getStringExtra(CITY));
        cityTw.setOnLongClickListener(infoClick);

        final Handler handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                //температура
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        //Александр! Подскажите пожалуйста! не могу понять почему возникает ошибка NetworkOnMainThreadException!
                        //Ведь работа с сетью происходит не в главном потоке.
                        //Из-за проблемы подключения к интернету работа встала, так как не могу тестировать приложение
                        //И, соответственно, не могу двигаться дальше! Странно, я пытался изменить gradle-файл, изменив библиотеку andriodx на
                        //support, в таком случае с представленном кодом удавалось получать реальные данные!
                        //Однако, я полагаю это неправильный подоход пользоваться более старыми библиотеками! Хотелось бы разобраться с использованием androidx.
                        final WeatherData wd = new WeatherData();
                        //Toast.makeText(getBaseContext(), Double.toString(wd.getTemperature()),Toast.LENGTH_SHORT).show();
                        double t = wd.getTemperature();//etData().getMain().getTemperature();//rn.nextInt(61)-30;
                        TextView tvTemperature = findViewById(R.id.tvw_temperature_now);
                        tvTemperature.setText(String.format(Locale.ROOT, "%s%.0f \u00B0C", t > 0 ? "+" : "", t));
                        tvTemperature.setTextColor(getRGB((int) t, -30, 30));
                        //небо
                        int cl = rn.nextInt(t < 0 ? 6 : 5);
                        TextView tvSky = findViewById(R.id.tvw_sky);
                        tvSky.setText(resources.getStringArray(R.array.txt_sky)[cl]);
                        ImageView ivSky = findViewById(R.id.img_sky);
                        ivSky.setImageResource(imgSkyArray[cl]);

                        boolean settings = getIntent().getBooleanExtra(SETTINGS, true);
                        if (settings) {
                            FragmentTransaction fTrans;
                            fTrans = getSupportFragmentManager().beginTransaction();
                            //получение случайных данных
                            int windSpeed = wd.getWindSpeed();//rn.nextInt(15);
                            int windDirection = //windSpeed == 0 ? 0 :
                                    wd.getWindDeg();/// / 8 + 1;//rn.nextInt(8) + 1;
                            int humidity = wd.getHumidity();//WeatherData.getData().getMain().getHumidity();//rn.nextInt(101);
                            int pressure = wd.getPressure();//WeatherData.getData().getMain().getHumidity();//rn.nextInt(101) + 700;
                            AdditionalParametersFragment addParamFrag = AdditionalParametersFragment.create(
                                    windDirection, windSpeed, humidity, pressure);
                            fTrans.add(R.id.additional_parameters, addParamFrag);
                            fTrans.commit();
                        }
                    }
                });
            }
        }).start();
    }

    // Чтение настроек, параметр тема
    protected boolean isDarkTheme() {
        // Работаем через специальный класс сохранения и чтения настроек
        SharedPreferences sharedPref = getSharedPreferences(NameSharedPreference, MODE_PRIVATE);
        //Прочитать тему, если настройка не найдена - взять по умолчанию true
        return sharedPref.getBoolean(IsDarkTheme, true);
    }

    // метод определяет цвет в спектре от синего до красного
    public static int getRGB(int x, int t0, int t) {
        int r, g, b;
        int dt = (t - t0);
        if (x < t0) {
            r = g = 0;
            b = 255;
        } else if (x < t0 + dt / 4) {
            r = 0;
            g = 1024 * (x - t0) / dt;
            b = 255;
        } else if (x < t0 + dt / 2) {
            r = 0;
            g = 255;
            b = -1024 / dt * (x - t0 - dt / 2);
        } else if (x < t0 + 3 * dt / 4) {
            r = 1024 * (x - t0 - dt / 2) / dt;
            g = 255;
            b = 0;
        } else if (x < t) {
            r = 255;
            g = -1024 / dt * (x - t);
            b = 0;
        } else {
            r = 255;
            g = b = 0;
        }
        return Color.rgb(r, g, b);
    }
}