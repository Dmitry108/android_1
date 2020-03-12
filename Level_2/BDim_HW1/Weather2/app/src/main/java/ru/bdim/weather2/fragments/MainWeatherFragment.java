package ru.bdim.weather2.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import ru.bdim.weather2.R;
import ru.bdim.weather2.additional.Weather;
import ru.bdim.weather2.additional.Acsessorius;

import static ru.bdim.weather2.additional.Constants.WEATHER;

public class MainWeatherFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_main_weather, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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
            Bundle args = getArguments();
            Weather weather = null;
            if (args != null) {
                Toast.makeText(view.getContext(), "парсел не нал", Toast.LENGTH_SHORT).show();
                weather = (Weather) args.getSerializable(WEATHER);
                if (weather != null) {
                    //дата
                    TextView date = view.findViewById(R.id.tvw_date_today);
                    DateFormat dateFormat = new SimpleDateFormat("d MMMM yyyy", Locale.getDefault());
                    Date dateToday = new Date();
                    date.setText(dateFormat.format(dateToday));
                    date.setOnLongClickListener(infoListener);
                    //город
                    TextView tvCity = view.findViewById(R.id.tvw_city);
                    tvCity.setText(weather.getCity());
                    tvCity.setOnLongClickListener(infoListener);
                    //температура
                    int t = weather.getTemperature();
                    TextView tvTemperature = view.findViewById(R.id.tvw_temperature_now);
                    tvTemperature.setText(String.format(Locale.ROOT, "%s%d \u00B0C", t > 0 ? "+" : "", t));
                    tvTemperature.setTextColor(Acsessorius.getRGB(t, -30, 30));
                    //небо
                    int cl = weather.getSky();
                    TextView tvSky = view.findViewById(R.id.tvw_sky);
                    tvSky.setText(getResources().getStringArray(R.array.txt_sky)[cl]);
                    ImageView ivSky = view.findViewById(R.id.img_sky);
                    @SuppressLint("Recycle")
                    TypedArray imageArray = getResources().obtainTypedArray(R.array.img_sky);
                    ivSky.setImageResource(imageArray.getResourceId(cl, -1));
                }
            }
        }
    }
}