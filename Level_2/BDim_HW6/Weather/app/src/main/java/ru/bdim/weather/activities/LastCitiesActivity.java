package ru.bdim.weather.activities;

import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import ru.bdim.weather.R;
import ru.bdim.weather.addiyional.Constants;
import ru.bdim.weather.addiyional.RecyclerAdapter;
import ru.bdim.weather.data.Weather;
import ru.bdim.weather.fragments.LastCitiesFragment;

public class LastCitiesActivity extends AppCompatActivity implements Constants {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_city);

        if (Weather.getWeatherListSize() > 0) {
            FragmentManager fm = getSupportFragmentManager();
            Fragment fragment = new LastCitiesFragment();
            fm.beginTransaction()
                    .replace(R.id.flt_last_cities, fragment)
                    .commit();
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_options_select_city, menu);
        RecyclerView recyclerView = findViewById(R.id.rcv_last_cities);
        final RecyclerAdapter adapter = (RecyclerAdapter) recyclerView.getAdapter();
        MenuItem search = menu.findItem(R.id.mob_search);
        SearchView searchView = (SearchView)search.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                if (adapter != null) {
                    adapter.search(newText);
                }
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        RecyclerView recyclerView = findViewById(R.id.rcv_last_cities);
        RecyclerAdapter adapter = (RecyclerAdapter) recyclerView.getAdapter();
        switch (item.getItemId()) {
            case android.R.id.home:
                //не забыть обработать передачу погоды из searchView
                finish();
                break;
            case R.id.mob_sort_by_city:
                if (adapter != null){
                    adapter.sortByCity();
                }
                break;
            case R.id.mob_sort_by_date:
                if (adapter != null){
                    adapter.sortByDate();
                }
                break;
            case R.id.mob_sort_by_temp:
                if (adapter != null){
                    adapter.sortByTemp();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.menu_context_last_citirs_list, menu);
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        RecyclerView recyclerView = findViewById(R.id.rcv_last_cities);
        RecyclerAdapter adapter = (RecyclerAdapter) recyclerView.getAdapter();
        if (adapter != null) {
            switch (item.getItemId()) {
                case R.id.mct_open:
                    adapter.openItem();
                    return true;
                case R.id.mct_delete:
                    adapter.removeItem();
                    return true;
            }
        }
        return super.onContextItemSelected(item);
    }
//    public void backToMainActivity(CurrentWeather weather){
//        Intent intent = new Intent(LastCitiesActivity.this, MainActivity.class);
//        intent.putExtra(CITY, weather.getCity());
//        setResult(SELECT_CITY_RESULT_CODE, intent);
//        finish();
//    }
}