package com.github.kko7.manaflux_android.UserInterface.Dashboard;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.kko7.manaflux_android.Helpers.PrefsHelper;
import com.github.kko7.manaflux_android.R;

public class SettingsFragment extends Fragment {

    private static final String TAG = SettingsFragment.class.getSimpleName();
    PrefsHelper prefsHelper;
    Button deleteDeviceBtn, deleteAllBtn;
    Spinner spinner;
    RelativeLayout layout;

    public SettingsFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, Bundle savedInstanceState) {
        Log.d(TAG, "onView: Created");

        prefsHelper = PrefsHelper.getInstance(view.getContext());
        deleteDeviceBtn = view.findViewById(R.id.delete_device);
        deleteAllBtn = view.findViewById(R.id.delete_all);
        spinner = view.findViewById(R.id.spinner);
        layout = view.findViewById(R.id.settings_layout);

        deleteDeviceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(view.getContext(), "Not ready", Toast.LENGTH_SHORT).show();
            }
        });

        deleteAllBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(view.getContext(), "Not ready", Toast.LENGTH_LONG).show();
            }
        });

        String[] items = new String[]{
                getString(R.string.background_default), //default
                getString(R.string.background_red), //red
                getString(R.string.background_purple), //purple
                getString(R.string.background_green), //green
                getString(R.string.background_gray), //purple
                getString(R.string.background_dark_red)}; // dark red
        ArrayAdapter adapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_spinner_dropdown_item, items);
        spinner.setAdapter(adapter);
        spinner.setSelection(prefsHelper.getInt("spinner"));

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
                switch (position) {
                    case 0:
                        prefsHelper.saveString("background", "default");
                        prefsHelper.saveInt("spinner", 0);
                        break;
                    case 1:
                        prefsHelper.saveString("background", "red");
                        prefsHelper.saveInt("spinner", 1);
                        break;
                    case 2:
                        prefsHelper.saveString("background", "purple");
                        prefsHelper.saveInt("spinner", 2);
                        break;
                    case 3:
                        prefsHelper.saveString("background", "green");
                        prefsHelper.saveInt("spinner", 3);
                        break;
                    case 4:
                        prefsHelper.saveString("background", "gray");
                        prefsHelper.saveInt("spinner", 4);
                        break;
                    case 5:
                        prefsHelper.saveString("background", "red_dark");
                        prefsHelper.saveInt("spinner", 5);
                        break;
                    default:
                        prefsHelper.saveString("background", "default");
                        prefsHelper.saveInt("spinner", 0);
                        break;

                }
                String value = prefsHelper.getBackground("background");
                int res = getResources().getIdentifier(value + "_bg", "mipmap", view.getContext().getPackageName());
                layout.setBackgroundResource(res);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //TODO
            }
        });
    }
}
