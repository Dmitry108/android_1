package ru.bdim.weather.addiyional;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ru.bdim.weather.R;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private OnItemClickListener clickListener;
    private OnItemContextMenu contextMenu;
    private View selectedItem;
    private int selectedPosition;

    public void setItemInterface(OnItemClickListener clickListener, OnItemContextMenu contextMenu) {
        this.clickListener = clickListener;
        this.contextMenu = contextMenu;
    }

    @NonNull
    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_last_cities, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.ViewHolder holder, int position) {
        TextView tvwCity = holder.itemView.findViewById(R.id.tvw_last_city);
        TextView tvwDate = holder.itemView.findViewById(R.id.tvw_last_date);
        TextView tvwTemp = holder.itemView.findViewById(R.id.tvw_last_temp);
        ImageView imgSky = holder.itemView.findViewById(R.id.img_last_sky);
        Weather weather = LastCitiesPresenter.getInstance().getWeatherList().get(position);
        tvwCity.setText(weather.getCity());
        tvwDate.setText(weather.getDate());
        tvwTemp.setText(Format.getTempC(weather.getTemperature()));
        imgSky.setImageResource(Format.geiImgSky(imgSky, weather.getSkyIcon()));
    }

    @Override
    public int getItemCount() {
        return LastCitiesPresenter.getInstance().getWeatherList().size();
    }

    public void removeItem() {
        LastCitiesPresenter.getInstance().removeItem(selectedPosition);
        notifyItemRemoved(selectedPosition);
    }
    public void sortByCity() {
        LastCitiesPresenter.getInstance().sortByCity();
        notifyItemRangeChanged(0, getItemCount());
    }

    public void sortByDate() {
        LastCitiesPresenter.getInstance().sortByDate();
        notifyItemRangeChanged(0, getItemCount());
    }

    public void sortByTemp() {
        LastCitiesPresenter.getInstance().sortByTemp();
        notifyItemRangeChanged(0, getItemCount());
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onClickListener(v, getAdapterPosition());
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    selectedItem = v;
                    selectedPosition = getAdapterPosition();
                    return false;
                }
            });
            contextMenu.registerContextMenu(itemView);
        }
    }
    public View getSelectedItem(){
        return selectedItem;
    }

    public interface OnItemClickListener {
        void onClickListener(View view, int position);
    }
    public interface OnItemContextMenu {
        void registerContextMenu(View view);
    }
}