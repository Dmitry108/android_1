package ru.bdim.weather.fragments;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.Locale;

import ru.bdim.weather.R;
import ru.bdim.weather.addiyional.Format;
import ru.bdim.weather.data.CurrentWeather;
import ru.bdim.weather.data.Data;
import ru.bdim.weather.patterns.Observer;

public class MainWeatherFragment extends Fragment implements Observer {
    private CurrentWeather weather;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_main_weather, container, false);
    }
    @Override
    public void onResume() {
        super.onResume();

        final View view = getView();
        if (view != null) {
            //обработка событий
            View.OnLongClickListener infoListener = new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    String str = ((TextView) v).getText().toString();
                    if (v.getId() == R.id.tvw_date_today) {
                        str = str.substring(0, str.length() - 5);
                    }
                    Uri uri = Uri.parse(String.format(Locale.ROOT,
                            "%s%s", getResources().getString(R.string.wiki), str));
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    ActivityInfo isIntentOk = intent.resolveActivityInfo(
                            v.getContext().getPackageManager(), intent.getFlags());
                    if (isIntentOk != null) {
                        startActivity(intent);
                    } else {
                        Toast.makeText(getContext(),
                                getResources().getString(R.string.browser_abs),
                                Toast.LENGTH_SHORT).show();
                    }
                    return false;
                }
            };
            //Извлечение данных
            if (weather != null) {
                //дата
                TextView date = view.findViewById(R.id.tvw_date_today);
                date.setText(weather.getDate());
                date.setOnLongClickListener(infoListener);
                //город
                TextView tvwCity = view.findViewById(R.id.tvw_city);
                tvwCity.setText(weather.getCity());
                tvwCity.setOnLongClickListener(infoListener);
                //температура
                int t = weather.getTemperature();
                TextView tvwTemp = view.findViewById(R.id.tvw_temperature_now);
                tvwTemp.setText(Format.getTempC(t));
                tvwTemp.setTextColor(Format.getRGB(t, -30, 30));
                //небо
                //int cl = weather.getSky();
                TextView tvwSky = view.findViewById(R.id.tvw_sky);
                tvwSky.setText(weather.getSkyDescription());
                ImageView imvSky = view.findViewById(R.id.img_sky);
                imvSky.setImageResource(Format.geiImgSky(imvSky, weather.getSky()));
            }
        }
    }
    @Override
    public void updateWeather(Data weather) {
        this.weather = (CurrentWeather) weather;
        onResume();
    }
}