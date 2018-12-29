package com.github.kko7.manaflux_android.UserInterface;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.RelativeLayout;

import com.github.kko7.manaflux_android.Helpers.PrefsHelper;
import com.github.kko7.manaflux_android.R;

public class LoadActivity extends AppCompatActivity {

    private static final String TAG = "LoadActivity";
    PrefsHelper prefsHelper;
    RelativeLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);
        Log.d(TAG, "onCreate: Started.");
        init();
        setBackground();
    }

    private void init() {
        prefsHelper = PrefsHelper.getInstance(this);
        prefsHelper = PrefsHelper.getInstance(this);
        layout = findViewById(R.id.settings_layout);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    private void setBackground() {
        String value = (prefsHelper.getBackground("background"));
        int id = getResources().getIdentifier(value + "_bg", "mipmap", getPackageName());
        layout.setBackgroundResource(id);
    }

}