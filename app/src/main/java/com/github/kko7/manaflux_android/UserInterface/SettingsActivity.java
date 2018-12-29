package com.github.kko7.manaflux_android.UserInterface;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.github.kko7.manaflux_android.Helpers.PrefsHelper;
import com.github.kko7.manaflux_android.R;

public class SettingsActivity extends AppCompatActivity {

    private static final String TAG = "Settings";
    PrefsHelper prefsHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Log.d(TAG, "onCreate: Started.");
        prefsHelper = PrefsHelper.getInstance(this);
        setBackground();
        initList();
    }

    protected void initList() {
        Spinner spinner = findViewById(R.id.spinner);
        String[] items = new String[]{"Default", "Red", "Purple"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        spinner.setAdapter(adapter);
        spinner.setSelection(prefsHelper.getDataInt("spinner"));

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        prefsHelper.saveDataString("background", "default");
                        prefsHelper.saveDataInt("spinner", 0);
                        setBackground();
                        break;
                    case 1:
                        prefsHelper.saveDataString("background", "red");
                        prefsHelper.saveDataInt("spinner", 1);
                        setBackground();
                        break;
                    case 2:
                        prefsHelper.saveDataString("background", "purple");
                        prefsHelper.saveDataInt("spinner", 2);
                        setBackground();
                        break;
                    default:
                        prefsHelper.saveDataString("background", "default");
                        prefsHelper.saveDataInt("spinner", 0);
                        setBackground();
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //TODO
            }
        });
    }

    protected void setBackground() {
        String value = (prefsHelper.getDataString("background"));
        int id = getResources().getIdentifier(value + "_bg", "mipmap", getPackageName());
        RelativeLayout layout = findViewById(R.id.settings_layout);
        layout.setBackgroundResource(id);
    }


}