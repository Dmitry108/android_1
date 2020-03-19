package ru.bdim.weather;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.Locale;

public class AdditionalParametersFragment extends Fragment {
    private static final String DIRECTION = "direction";
    private static final String SPEED = "speed";
    private static final String HUMIDITY = "humidity";
    private static final String PRESSURE = "pressure";

    public static AdditionalParametersFragment create(int windDirection, int windSpeed, int humidity, int pressure){
        AdditionalParametersFragment addParamFrag = new AdditionalParametersFragment();
        Bundle args = new Bundle();
        args.putInt(DIRECTION, windDirection);
        args.putInt(SPEED, windSpeed);
        args.putInt(HUMIDITY, humidity);
        args.putInt(PRESSURE, pressure);
        addParamFrag.setArguments(args);
        return addParamFrag;
    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_additional_parameters,null);
        Resources resources = getResources();
        //ветер
        int v = getArguments().getInt(SPEED);
        int dir = getArguments().getInt(DIRECTION);
        TextView tvwWind = view.findViewById(R.id.tvw_wind);
        tvwWind.setText(String.format(Locale.ROOT, "%d\u00B0 %d %s", dir, v,
               // resources.getStringArray(R.array.wind_direction)[dir], v,
                resources.getString(R.string.speed)));
        //влажность
        int h = getArguments().getInt(HUMIDITY);
        TextView tvwHumidity = view.findViewById(R.id.tvw_humidity);
        tvwHumidity.setText(String.format(Locale.ROOT,"%d %%", h));
        //давление
        int p = getArguments().getInt(PRESSURE);
        TextView tvwPressure = view.findViewById(R.id.tvw_pressure);
        tvwPressure.setText(String.format(Locale.ROOT, "%d %s", p,
                resources.getString(R.string.press)));
        return view;
    }
}
