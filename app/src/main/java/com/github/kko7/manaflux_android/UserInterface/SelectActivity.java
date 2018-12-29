package com.github.kko7.manaflux_android.UserInterface;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.github.kko7.manaflux_android.Helpers.PrefsHelper;
import com.github.kko7.manaflux_android.R;

public class SelectActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    RelativeLayout layout;
    PrefsHelper prefsHelper;
    Button settingsButton, otherButton, refreshButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
        Log.d(TAG, "onCreate: Started.");
        initViews();
        initButtons();
        setBackground();
    }

    protected void setBackground() {
        String value = prefsHelper.getBackground("background");
        int id = getResources().getIdentifier(value + "_bg", "mipmap", getPackageName());
        layout.setBackgroundResource(id);
    }

    protected void initButtons(){
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SelectActivity.this, SettingsActivity.class));
            }
        });
    }

    protected void initViews(){
        prefsHelper = PrefsHelper.getInstance(this);
        settingsButton = findViewById(R.id.settings_button);
        layout = findViewById(R.id.select_layout);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setBackground();
    }


}
