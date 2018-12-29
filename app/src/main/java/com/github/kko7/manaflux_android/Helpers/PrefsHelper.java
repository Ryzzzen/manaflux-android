package com.github.kko7.manaflux_android.Helpers;

import android.content.Context;
import android.content.SharedPreferences;

import com.github.kko7.manaflux_android.R;

import static android.content.Context.MODE_PRIVATE;

public class PrefsHelper {
    private static PrefsHelper prefsHelper;
    private SharedPreferences sharedPreferences;

    public static PrefsHelper getInstance(Context context) {
        if (prefsHelper == null) {
            prefsHelper = new PrefsHelper(context);
        }
        return prefsHelper;
    }

    private PrefsHelper(Context context) {
        sharedPreferences = context.getSharedPreferences("Settings", MODE_PRIVATE);
    }

    public void saveString(String key, String value) {
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.putString(key, value);
        prefsEditor.apply();
    }

    public void saveInt(String key, int value) {
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.putInt(key, value);
        prefsEditor.apply();
    }

    public String getBackground(String key) {
        return sharedPreferences.getString(key, "default");
    }

    public String getString(String key) {
        return sharedPreferences.getString(key, null);
    }

    public int getInt(String key) {
        return sharedPreferences.getInt(key, 0);
    }
}
