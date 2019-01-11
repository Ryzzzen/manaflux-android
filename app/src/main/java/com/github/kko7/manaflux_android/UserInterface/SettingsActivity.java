package com.github.kko7.manaflux_android.UserInterface;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.kko7.manaflux_android.Helpers.PrefsHelper;
import com.github.kko7.manaflux_android.R;

public class SettingsActivity extends AppCompatActivity {

    private static final String TAG = "Settings";
    PrefsHelper prefsHelper;
    Button deleteDeviceBtn, deleteAllBtn;
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Log.d(TAG, "onCreate: Started.");
        init();
        initButtons();
        initSpinner();
        setBackground();
    }

    protected void init() {
        prefsHelper = PrefsHelper.getInstance(this);
        deleteDeviceBtn = findViewById(R.id.delete_device);
        deleteAllBtn = findViewById(R.id.delete_all);
        spinner = findViewById(R.id.spinner);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    protected void initButtons() {
        deleteDeviceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //prefsHelper.saveString("deviceID", "");
                //prefsHelper.saveString("deviceNAME", "");
                //startActivity(new Intent(SettingsActivity.this, MainActivity.class));
                Toast.makeText(SettingsActivity.this, "Not ready", Toast.LENGTH_SHORT).show();
            }
        });

        deleteAllBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Not ready", Toast.LENGTH_LONG).show();
            }
        });
    }

    protected void initSpinner() {
        String[] items = new String[]{
                getString(R.string.background_default), //default
                getString(R.string.background_red), //red
                getString(R.string.background_purple), //purple
                getString(R.string.background_green), //green
                getString(R.string.background_purple), //purple
                getString(R.string.background_dark_red)}; // dark red
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        spinner.setAdapter(adapter);
        spinner.setSelection(prefsHelper.getInt("spinner"));

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        prefsHelper.saveString("background", "default");
                        prefsHelper.saveInt("spinner", 0);
                        setBackground();
                        break;
                    case 1:
                        prefsHelper.saveString("background", "red");
                        prefsHelper.saveInt("spinner", 1);
                        setBackground();
                        break;
                    case 2:
                        prefsHelper.saveString("background", "purple");
                        prefsHelper.saveInt("spinner", 2);
                        setBackground();
                        break;
                    case 3:
                        prefsHelper.saveString("background", "green");
                        prefsHelper.saveInt("spinner", 3);
                        setBackground();
                        break;
                    case 4:
                        prefsHelper.saveString("background", "gray");
                        prefsHelper.saveInt("spinner", 4);
                        setBackground();
                        break;
                    case 5:
                        prefsHelper.saveString("background", "red_dark");
                        prefsHelper.saveInt("spinner", 5);
                        setBackground();
                        break;
                    default:
                        prefsHelper.saveString("background", "default");
                        prefsHelper.saveInt("spinner", 0);
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
        String value = (prefsHelper.getBackground("background"));
        int id = getResources().getIdentifier(value + "_bg", "mipmap", getPackageName());
        RelativeLayout layout = findViewById(R.id.settings_layout);
        layout.setBackgroundResource(id);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setBackground();
    }
}