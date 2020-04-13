package ru.bdim.weather.dialogs;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Objects;

import ru.bdim.weather.R;
import ru.bdim.weather.addiyional.Constants;

public class EnterCityDialogFragment extends DialogFragment implements Constants {

    private CheckBox cbxExtra;
    private CheckBox cbxHourly;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.dialog_fragment_enter_city, container, false);

        final AutoCompleteTextView actCity = view.findViewById(R.id.tvw_enter_city);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(view.getContext(), R.layout.support_simple_spinner_dropdown_item,
                view.getResources().getStringArray(R.array.pop_cities));
        actCity.setAdapter(adapter);

        cbxExtra = view.findViewById(R.id.cbx_extra_parameters);
        cbxHourly = view.findViewById(R.id.cbx_hourly_forecast);
        Bundle args = getArguments();

        if (args != null){
            cbxExtra.setChecked(args.getBoolean(EXTRA));
            cbxHourly.setChecked(args.getBoolean(HOURLY));
        }

        Button btnLocation = view.findViewById(R.id.btn_current_location);
        btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                Log.d(TAG, "нажата кнопка по местоположению");
                restartMainActivity("@coord");
            }
        });
        Button btnOk = view.findViewById(R.id.btn_ok_enter_city);
        Button btnCancel = view.findViewById(R.id.btn_cancel_enter_city);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                String city = actCity.getText().toString();
                restartMainActivity(city);
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return view;
    }

    public void restartMainActivity(String city) {
        Intent intent = new Intent();
        intent.putExtra(CITY, city);
        intent.putExtra(EXTRA, cbxExtra.isChecked());
        intent.putExtra(HOURLY, cbxHourly.isChecked());
        Objects.requireNonNull(getActivity())
            .onActivityReenter(ENTER_CITY_RESULT_CODE, intent);
}}