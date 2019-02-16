package com.github.kko7.manaflux_android.Services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.github.kko7.manaflux_android.Connection.ApiClient;
import com.github.kko7.manaflux_android.Connection.ApiInterface;
import com.github.kko7.manaflux_android.Helpers.PrefsHelper;
import com.github.kko7.manaflux_android.Models.ApiData;
import com.github.kko7.manaflux_android.R;
import com.github.kko7.manaflux_android.UserInterface.ChampionSelectActivity;

import io.socket.client.Socket;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NotificationsService extends Service {

    Socket mSocket;
    Context context = getApplicationContext();
    NotificationsService self;
    PrefsHelper prefsHelper;

    public NotificationsService() {
    }

    @Override
    public void onCreate() {
        self = this;
    }

    public void createNotification(String title, String text) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context, "1")
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(title)
                        .setContentText(text);
        Intent resultIntent = new Intent(this, ChampionSelectActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(ChampionSelectActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, mBuilder.build());

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                final ApiInterface client = new ApiClient(self).getClient();
                Call<ApiData> testConnection = client.testConnection();
                testConnection.enqueue(new Callback<ApiData>() {
                    @Override
                    public void onResponse(Call<ApiData> call, Response<ApiData> response) {
                        if (response.isSuccessful()) {

                        }
                    }

                    @Override
                    public void onFailure(Call<ApiData> call, Throwable t) {

                    }
                });
            }
        }, 1000);
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        mSocket.disconnect();
        prefsHelper.saveBoolean("service-stopped", true);

    }
}