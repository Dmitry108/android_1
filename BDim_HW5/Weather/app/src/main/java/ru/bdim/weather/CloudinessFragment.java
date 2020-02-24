package ru.bdim.weather;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class CloudinessFragment extends Fragment {
    private static final String CLOUDS = "clouds";

    public static CloudinessFragment create(int clouds){
        CloudinessFragment clFrag = new CloudinessFragment();
        Bundle args = new Bundle();
        args.putInt(CLOUDS, clouds);
        clFrag.setArguments(args);
        return clFrag;
    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_cloudiness,null);
        TextView clouds = view.findViewById(R.id.clouds);
        int cl = getArguments().getInt(CLOUDS);
        clouds.setText(getResources().getString(cl == 0 ? R.string.clear:
                cl == 1 ? R.string.cloudly : R.string.rain));
        ImageView cloudsImage = view.findViewById(R.id.imageView);
        cloudsImage.setImageResource(cl==0 ? R.drawable.sun:
                cl == 1 ? R.drawable.cloud : R.drawable.rain);
        return view;
    }




}
