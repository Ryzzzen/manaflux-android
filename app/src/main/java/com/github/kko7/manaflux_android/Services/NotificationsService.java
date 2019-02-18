package com.github.kko7.manaflux_android.Services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class NotificationsService extends Service {

    NotificationsService self;

    public NotificationsService() {
    }

    @Override
    public void onCreate() {
        self = this;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
    }
}