package com.github.kko7.manaflux_android;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.kko7.manaflux_android.Connection.FirebaseMessagingService;
import com.github.kko7.manaflux_android.Helpers.PrefsHelper;
import com.github.kko7.manaflux_android.UserInterface.DashboardActivity;
import com.github.kko7.manaflux_android.UserInterface.SelectActivity;

public class MainActivity extends AppCompatActivity {

    Intent intent;
    PrefsHelper prefsHelper;
    FirebaseMessagingService firebaseMessagingService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
        checkDevice();
        prefsHelper = PrefsHelper.getInstance(this);
    }

    protected void checkDevice() {
        PrefsHelper prefsHelper = PrefsHelper.getInstance(this);
        String deviceIP = prefsHelper.getString("DeviceIP");
        if(deviceIP == null || deviceIP.equals("")) {
            intent = new Intent(MainActivity.this, SelectActivity.class);
        } else {
            intent = new Intent(MainActivity.this, DashboardActivity.class);
        }
        startActivity(intent);
    }

    protected void initViews() {
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
}
