package ru.bdim.weather2.additional;

import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

import ru.bdim.weather2.R;

// Адаптер
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    private List<Weather> dataSource;
    private OnItemClickListener itemClickListener;
    // Слушатель будет устанавливаться извне

    // Передаем в конструктор источник данных
    // В нашем случае это массив, но может быть и запросом к БД
    public RecyclerAdapter(List<Weather> dataSource){
        this.dataSource = dataSource;
    }

    // Создать новый элемент пользовательского интерфейса
    // Запускается менеджером
    @NonNull
    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // Создаем новый элемент пользовательского интерфейса
        // Через Inflater
        View v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_used_cities, viewGroup, false);
        // Здесь можно установить всякие параметры
        return new ViewHolder(v);
    }

    // Заменить данные в пользовательском интерфейсе
    // Вызывается менеджером
    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.ViewHolder viewHolder, int i) {
        // Получить элемент из источника данных (БД, интернет...)
        // Вынести на экран используя ViewHolder
        TextView tvwCity = viewHolder.getLayout().findViewById(R.id.tvw_list_city);
        TextView tvwDate = viewHolder.getLayout().findViewById(R.id.tvw_list_date);
        TextView tvwTemp = viewHolder.getLayout().findViewById(R.id.tvw_list_temp);
        ImageView imgSky = viewHolder.getLayout().findViewById(R.id.img_list_sky);
        Weather weather = dataSource.get(i);
        tvwCity.setText(weather.getCity());
        tvwDate.setText(weather.getDate());
        tvwTemp.setText(String.format(Locale.ROOT, "%d", weather.getTemperature()));
        TypedArray imageArray = imgSky.getResources().obtainTypedArray(R.array.img_sky);
        imgSky.setImageResource(imageArray.getResourceId(weather.getSky(), -1));
    }

    // Вернуть размер данных, вызывается менеджером
    @Override
    public int getItemCount() {
        return dataSource.size();
    }

    // Интерфейс для обработки нажатий как в ListView
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    // Сеттер слушателя нажатий
    public void setOnItemClickListener(OnItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }

    // Этот класс хранит связь между данными и элементами View
    // Сложные данные могут потребовать несколько View на
    // один пункт списка
    public class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            layout = (LinearLayout) itemView;
            //textView = (TextView) itemView;
            // Обработчик нажатий на этом ViewHolder
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemClickListener != null)
                        itemClickListener.onItemClick(v, getAdapterPosition());
                }
            });
        }
        public LinearLayout getLayout() {
            return layout;
        }
    }
}
