package ru.bdim.weather;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.Locale;

public class PressureFragment extends Fragment {
    private static final String PRESSURE = "pressure";

    public static PressureFragment create (int p){
        PressureFragment pFrag = new PressureFragment();
        Bundle args = new Bundle();
        args.putInt(PRESSURE, p);
        pFrag.setArguments(args);
        return pFrag;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pressure, null);
        TextView pressure = view.findViewById(R.id.pressure);
        int p = getArguments().getInt(PRESSURE);
        pressure.setText(String.format(Locale.ROOT, "%s: %d %s",
                getResources().getString(R.string.pressure),
                p, getResources().getString(R.string.press)));
        return view;
    }
}