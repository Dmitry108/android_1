package ru.bdim.weather.dialogs;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Objects;

import ru.bdim.weather.R;
import ru.bdim.weather.addiyional.Constants;

public class SettingsDialogFragment extends DialogFragment implements Constants {
    private CheckBox cbxExtra;
    private CheckBox cbxHourly;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.dialog_fragment_settings, container, false);

        cbxExtra = view.findViewById(R.id.cbx_settings_extra_parameters);
        cbxHourly = view.findViewById(R.id.cbx_settings_hourly_forecast);
        Bundle args = getArguments();

        if (args != null){
            cbxExtra.setChecked(args.getBoolean(EXTRA));
            cbxHourly.setChecked(args.getBoolean(HOURLY));
        }

        Button btnOk = view.findViewById(R.id.btn_settings_ok);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                restartMainActivity();
            }
        });
        return view;
    }
    public void restartMainActivity() {
        Intent intent = new Intent();
        intent.putExtra(EXTRA, cbxExtra.isChecked());
        intent.putExtra(HOURLY, cbxHourly.isChecked());
        Objects.requireNonNull(getActivity())
                .onActivityReenter(SETTINGS_RESULT_CODE, intent);
    }
}