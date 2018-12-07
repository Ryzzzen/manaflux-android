package com.github.kko7.manaflux_android.UserInterface;

import android.content.Context;
import android.content.SharedPreferences;

import com.github.kko7.manaflux_android.R;

import static android.content.Context.MODE_PRIVATE;

public class SharedPrefs {
    private static SharedPrefs sharedPrefs;
    private SharedPreferences sharedPreferences;

    public static SharedPrefs getInstance(Context context) {
        if (sharedPrefs == null) {
            sharedPrefs = new SharedPrefs(context);
        }
        return sharedPrefs;
    }

    private SharedPrefs(Context context) {
        sharedPreferences = context.getSharedPreferences("Background", MODE_PRIVATE);
    }

    public void saveDataString(String key, String value) {
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.putString(key, value);
        prefsEditor.apply();
    }

    public void saveDataInt(String key, int value) {
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.putInt(key, value);
        prefsEditor.apply();
    }

    public String getDataString(String key) {
        return sharedPreferences.getString(key, String.valueOf(R.mipmap.default_bg));
    }

    public int getDataInt(String key) {
        return sharedPreferences.getInt(key, 0);
    }
}
