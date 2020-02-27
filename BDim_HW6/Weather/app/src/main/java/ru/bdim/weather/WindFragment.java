package ru.bdim.weather;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;

import java.util.Locale;

public class WindFragment extends Fragment {
    private static final String DIRECTION = "direction";
    private static final String SPEED = "speed";

    public static WindFragment create(int direction, int speed){
        WindFragment wFrag = new WindFragment();
        Bundle args = new Bundle();
        args.putInt(DIRECTION,direction);
        args.putInt(SPEED, speed);
        wFrag.setArguments(args);
        return wFrag;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_wind,null);
        TextView wind = view.findViewById(R.id.wind);
        int direction = getArguments().getInt(DIRECTION);
        int speed = getArguments().getInt(SPEED);
        wind.setText(String.format(Locale.ROOT,"%s: %s %d %s",
                getResources().getString(R.string.wind),
                (getResources().getTextArray(R.array.wind_direction)[direction]),
                speed, getResources().getString(R.string.speed)));
        return view;
    }
}