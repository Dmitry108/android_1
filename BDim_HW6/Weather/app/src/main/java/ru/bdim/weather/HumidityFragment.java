package ru.bdim.weather;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;

import java.util.Locale;

public class HumidityFragment extends Fragment {
    private static final String HUMIDITY = "humidity";

    public static HumidityFragment create(int h){
        HumidityFragment hFrag = new HumidityFragment();
        Bundle args = new Bundle();
        args.putInt(HUMIDITY, h);
        hFrag.setArguments(args);
        return hFrag;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_humidity, null);
        TextView humidity = view.findViewById(R.id.humidity);
        humidity.setText(String.format(Locale.ROOT,"%s: %d %%",
                getResources().getString(R.string.humidity),
                getArguments().getInt(HUMIDITY)));
        return view;
    }
}