package ru.bdim.weather.addiyional;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ru.bdim.weather.R;
import ru.bdim.weather.data.HourlyWeather;

public class HourlyWeatherAdapter
        extends RecyclerView.Adapter<HourlyWeatherAdapter.HourlyWeatherViewHolder> {
    private List<HourlyWeather> weatherList;

    public HourlyWeatherAdapter (List<HourlyWeather> weatherList){
        this.weatherList = weatherList;
    }

    @NonNull
    @Override
    public HourlyWeatherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_hourly_weather, parent, false);
        return new HourlyWeatherViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HourlyWeatherViewHolder holder, int position) {
        TextView tvwDate = holder.itemView.findViewById(R.id.tvw_hourly_time);
        TextView tvwTemp = holder.itemView.findViewById(R.id.tvw_hourly_temp);
        ImageView imgSky = holder.itemView.findViewById(R.id.img_hourly_sky);
        HourlyWeather weather = weatherList.get(position);
        tvwDate.setText(Format.timeToString(weather.getDate()));
        tvwTemp.setText(Format.getTempC(weather.getTemperature()));
        imgSky.setImageResource(Format.geiImgSky(imgSky, weather.getSky()));
    }

    @Override
    public int getItemCount() {
        return weatherList.size();
    }

    class HourlyWeatherViewHolder extends RecyclerView.ViewHolder{
        public HourlyWeatherViewHolder(@NonNull View itemView) {
            super(itemView);

        }
    }
}