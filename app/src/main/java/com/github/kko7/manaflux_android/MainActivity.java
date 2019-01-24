package com.github.kko7.manaflux_android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.kko7.manaflux_android.Helpers.PrefsHelper;
import com.github.kko7.manaflux_android.UserInterface.LanDevicesActivity;
import com.github.kko7.manaflux_android.UserInterface.LoadActivity;

import java.math.BigInteger;
import java.security.SecureRandom;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkDevice();
    }

    protected void checkDevice() {
        PrefsHelper prefsHelper = PrefsHelper.getInstance(this);
        String deviceIP = prefsHelper.getString("deviceIP");
        String deviceNAME = prefsHelper.getString("deviceNAME");
        String authToken = prefsHelper.getString("auth-token");

        if (authToken == null || authToken.equals("") || authToken.length() != 32) {
            String newToken = generateToken();
            prefsHelper.saveString("auth-token", newToken);
        }

        if (deviceIP == null || deviceIP.equals("") || deviceNAME == null || deviceNAME.equals("")) {
            startActivity(new Intent(MainActivity.this, LanDevicesActivity.class));
        } else {
            startActivity(new Intent(MainActivity.this, LoadActivity.class));
        }
    }

    protected String generateToken() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] token = new byte[16];
        secureRandom.nextBytes(token);
        return new BigInteger(1, token).toString(16);
    }
}
