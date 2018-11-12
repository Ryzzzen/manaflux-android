package com.github.ryzzzen.manaflux_android;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.util.Log;

import com.github.ryzzzen.manaflux_android.ActivityHandler.ScanActivity;
import com.github.ryzzzen.manaflux_android.ActivityHandler.SettingsActivity;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate: Started.");
        initViews();
    }

    public void gotoSettings(View view){
        Intent settingsIntent = new Intent(MainActivity.this, SettingsActivity.class);
        startActivity(settingsIntent);
    }

    private void initViews() {
        View scanButton = findViewById(R.id.scan_button);
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent scanIntent = new Intent(MainActivity.this, ScanActivity.class);
                startActivity(scanIntent);

                }
            }
         );

        View settingsButton = findViewById(R.id.settings_button);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent settingsIntent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(settingsIntent);

                }
            }
        );

    }
}