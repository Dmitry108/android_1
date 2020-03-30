package ru.bdim.weather.addiyional;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ru.bdim.weather.R;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    private List<Weather> data;
    private OnItemClickListener clickListener;

    public RecyclerAdapter(List<Weather> data) {
        this.data = data;
    }

    public void setClickListener(OnItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_last_cities, parent, false);
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.ViewHolder holder, int position) {
        TextView tvwCity = holder.itemView.findViewById(R.id.tvw_last_city);
        TextView tvwDate = holder.itemView.findViewById(R.id.tvw_last_date);
        TextView tvwTemp = holder.itemView.findViewById(R.id.tvw_last_temp);
        ImageView imgSky = holder.itemView.findViewById(R.id.img_last_sky);
        Weather weather = data.get(position);
        tvwCity.setText(weather.getCity());
        tvwDate.setText(weather.getDate());
        tvwTemp.setText(Format.getTempC(weather.getTemperature()));
        imgSky.setImageResource(Format.geiImgSky(imgSky, weather.getSkyIcon()));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            final LinearLayout item = (LinearLayout) itemView;
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onClickListener(v, getAdapterPosition());
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onClickListener(View view, int position);
    }
}