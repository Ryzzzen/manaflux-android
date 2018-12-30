package com.github.kko7.manaflux_android;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.github.kko7.manaflux_android.Helpers.PrefsHelper;
import com.github.kko7.manaflux_android.UserInterface.LoadActivity;
import com.github.kko7.manaflux_android.UserInterface.SelectActivity;

public class MainActivity extends AppCompatActivity {

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkDevice();
    }

    protected void checkDevice() {
        try {
            PrefsHelper prefsHelper = PrefsHelper.getInstance(this);
            String deviceIP = prefsHelper.getString("deviceIP");
            String deviceNAME = prefsHelper.getString("deviceNAME");

            if (deviceIP == null || deviceIP.equals("") || deviceNAME == null || deviceNAME.equals("")) {
                intent = new Intent(MainActivity.this, SelectActivity.class);
            } else {
                intent = new Intent(MainActivity.this, LoadActivity.class);
            }

            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
