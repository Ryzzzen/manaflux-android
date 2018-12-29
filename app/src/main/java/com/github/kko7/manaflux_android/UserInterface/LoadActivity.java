package com.github.kko7.manaflux_android.UserInterface;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.RelativeLayout;

import com.github.kko7.manaflux_android.Helpers.PrefsHelper;
import com.github.kko7.manaflux_android.R;

public class LoadActivity extends AppCompatActivity {

    private static final String TAG = "LoadActivity";
    PrefsHelper prefsHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);
        Log.d(TAG, "onCreate: Started.");
        prefsHelper = PrefsHelper.getInstance(this);
        setBackground();
    }

    protected void setBackground() {
        String value = (prefsHelper.getBackground("background"));
        int id = getResources().getIdentifier(value + "_bg", "mipmap", getPackageName());
        RelativeLayout layout = findViewById(R.id.settings_layout);
        layout.setBackgroundResource(id);
    }

}