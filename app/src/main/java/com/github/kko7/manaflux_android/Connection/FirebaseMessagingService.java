package com.github.kko7.manaflux_android.Connection;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.github.kko7.manaflux_android.Helpers.PrefsHelper;
import com.github.kko7.manaflux_android.MainActivity;
import com.github.kko7.manaflux_android.R;

import com.google.firebase.messaging.RemoteMessage;

import java.util.Objects;

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {
    private static final String TAG = "FirebaseMessaging";

    public FirebaseMessagingService() { }

    PrefsHelper prefsHelper = PrefsHelper.getInstance(this);
    String deviceIP = prefsHelper.getString("DeviceIP");
    String tokenIP = "http://" + deviceIP + ":4500/phone-token/";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        String title = Objects.requireNonNull(remoteMessage.getNotification()).getTitle();
        String message = remoteMessage.getNotification().getBody();
        Log.d(TAG, "onMessageReceived: Message Received: \n" +
                "Title: " + title + "\n" +
                "Message: " + message);

        sendNotification(title, message);
    }

    @Override
    public void onDeletedMessages() {
        //TODO
    }

    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);

        try {
            sendTokenToServer(token);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendTokenToServer(String token) throws Exception {
        HttpPost httpPost = new HttpPost(tokenIP, token);
        httpPost.run();
    }

    private void sendNotification(String title, String messageBody) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this /* support for oreo */)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(title)
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);


        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }
}