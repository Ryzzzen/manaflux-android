package com.github.kko7.manaflux_android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.kko7.manaflux_android.Helpers.PrefsHelper;
import com.github.kko7.manaflux_android.UserInterface.LanDevicesActivity;
import com.github.kko7.manaflux_android.UserInterface.LoadActivity;

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
                intent = new Intent(MainActivity.this, LanDevicesActivity.class);
            } else {
                intent = new Intent(MainActivity.this, LoadActivity.class);
            }

            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
