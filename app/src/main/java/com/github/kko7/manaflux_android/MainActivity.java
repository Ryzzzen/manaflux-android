package com.github.kko7.manaflux_android;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.kko7.manaflux_android.Helpers.PrefsHelper;
import com.github.kko7.manaflux_android.UserInterface.LoadActivity;
import com.github.kko7.manaflux_android.UserInterface.SelectActivity;

public class MainActivity extends AppCompatActivity {

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
        checkDevice();
    }

    protected void checkDevice() {
        PrefsHelper prefsHelper = PrefsHelper.getInstance(this);
        String deviceIP = prefsHelper.getString("deviceIP");
        if (deviceIP == null || deviceIP.equals("")) {
            intent = new Intent(MainActivity.this, SelectActivity.class);
        } else {
            intent = new Intent(MainActivity.this, LoadActivity.class);
        }
        startActivity(intent);
    }

    protected void initViews() {
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
}
