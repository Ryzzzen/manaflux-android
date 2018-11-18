package com.github.kko7.manaflux_android.ActivityHandler;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.github.kko7.manaflux_android.R;

public class ChangeActivity extends AppCompatActivity {
    private static final String TAG = "ConfigPc";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        Log.d(TAG, "onCreate: Started.");
        initButtons();
    }

    protected void initButtons() {
        Button scanButton = findViewById(R.id.scan_button);
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChangeActivity.this, ScanActivity.class));
            }
        });
    }
}
