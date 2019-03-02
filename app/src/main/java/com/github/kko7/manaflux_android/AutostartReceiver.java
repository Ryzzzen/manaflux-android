package com.github.kko7.manaflux_android;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.github.kko7.manaflux_android.Helpers.PrefsHelper;

public class AutostartReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Intent myIntent = new Intent(context, ChampionSelectService.class);
        if(PrefsHelper.getInstance(context).getBoolean("autostart")) {
            context.startService(myIntent);
        }
    }
}