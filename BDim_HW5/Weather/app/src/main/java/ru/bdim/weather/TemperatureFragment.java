package ru.bdim.weather;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class TemperatureFragment extends Fragment {
    private static final String TEMPERATURE = "temperature";

    public static TemperatureFragment create (int t){
        TemperatureFragment tFrag = new TemperatureFragment();
        Bundle args = new Bundle();
        args.putInt(TEMPERATURE, t);
        tFrag.setArguments(args);
        return tFrag;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_temperature, null);
        TextView t = view.findViewById(R.id.temperature);
        int tt = getArguments().getInt(TEMPERATURE);// presenter.getTemperature();
        t.setTextColor(getRGB(tt, -30, 30));
        t.setText(String.format("%s%dC", tt>0?"+":"", tt));
        return view;
    }

    // метод определяет цвет в спектре от синего до красного
    public static int getRGB (int x, int t0, int t){
        int r, g, b;
        int dt = (t - t0);
        if (x < t0){
            r = g = 0; b = 255;
        } else if (x < t0 + dt / 4) {
            r = 0; g = 1024*(x - t0)/dt; b = 255;
        } else if (x < t0 + dt/2) {
            r = 0; g = 255; b = -1024/dt*(x - t0 - dt/2);
        } else if (x < t0 + 3*dt/4) {
            r = 1024*(x - t0 - dt/2)/dt; g = 255; b = 0;
        } else if (x < t) {
            r = 255; g =-1024/dt*(x - t); b = 0;
        } else {
            r = 255; g = b = 0;
        }
        return Color.rgb(r, g, b);
    }
}
