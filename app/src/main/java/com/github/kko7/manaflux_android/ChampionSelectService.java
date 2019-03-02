package com.github.kko7.manaflux_android;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.github.kko7.manaflux_android.Helpers.PrefsHelper;
import com.github.kko7.manaflux_android.Models.HeartbeatData;
import com.github.kko7.manaflux_android.UserInterface.MainActivity;
import com.rx2androidnetworking.Rx2AndroidNetworking;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class ChampionSelectService extends Service {

    private String deviceIp;
    private String token;
    private NotificationManagerCompat notificationManager;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("ChampionSelectService", "onCreate: Started");
        deviceIp = PrefsHelper.getInstance(this).getString("device-ip");
        token = PrefsHelper.getInstance(this).getString("auth-token");
        AndroidNetworking.initialize(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        getData();
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void getData() {
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Rx2AndroidNetworking.get("http://" + deviceIp + ":4500/api/v1/me/heartbeat")
                        .addHeaders("Authorization", token)
                        .build()
                        .getObjectObservable(HeartbeatData.class)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .delay(1, TimeUnit.SECONDS)
                        .repeat()
                        .subscribe(new Observer<HeartbeatData>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(final HeartbeatData heartbeatData) {
                                if (heartbeatData.getInChampionSelect()) {
                                    sendDataToActivity(heartbeatData.getInChampionSelect(), heartbeatData.getChampionName(), heartbeatData.getChampionImg());
                                    showNotification();
                                } else {
                                    try {
                                        notificationManager.cancel(0);
                                    } catch (Exception ignored) {

                                    }
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e(ChampionSelectService.class.getSimpleName(), e.getMessage());
                                getData();
                            }

                            @Override
                            public void onComplete() {
                                Log.d(ChampionSelectService.class.getSimpleName(), "Completed");
                                getData();
                            }
                        });
            }
        };
        Thread workThread = new Thread(runnable);
        if(mWifi.isConnected()) {
            workThread.start();
        } else {
            try {
                workThread.interrupt();
                Thread.sleep(1000);
                getData();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void sendDataToActivity(boolean inChampionSelect, String name, String img) {
        Intent intent = new Intent();
        intent.setAction("GET_CHAMPION_SELECT_DATA");
        intent.putExtra("inChampionSelect", inChampionSelect);
        intent.putExtra("championName", name);
        intent.putExtra("championImage", img);
        sendBroadcast(intent);
    }

    private void showNotification() {
        notificationManager = NotificationManagerCompat.from(this);
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("class", true);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "1")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(getString(R.string.champion_in))
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(false);

        Notification n = builder.build();
        n.flags |= Notification.FLAG_NO_CLEAR | Notification.FLAG_ONGOING_EVENT;

        notificationManager.notify(0, n);
    }
}