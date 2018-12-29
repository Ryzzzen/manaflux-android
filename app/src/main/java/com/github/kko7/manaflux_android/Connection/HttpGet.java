package com.github.kko7.manaflux_android.Connection;

import android.support.annotation.NonNull;
import android.util.Log;

import java.io.IOException;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public final class HttpGet {
    private final String TAG = "HttpGet";
    private final OkHttpClient client = new OkHttpClient();
    private final String url;

    public HttpGet(String url) {
        this.url = url;
    }

    public void run() throws Exception {
        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful())
                        throw new IOException("Unexpected code " + response);

                    Log.d(TAG, Objects.requireNonNull(responseBody).string());
                }
            }
        });
    }
}