package com.github.kko7.manaflux_android.Connection;

import android.support.annotation.NonNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public final class HttpGet {
    private final OkHttpClient client = new OkHttpClient();
    private final String url;
    private HttpListener listener;

    public HttpGet(String url, HttpListener httpListener) {
        this.url = url;
        this.listener = httpListener;
    }

    public void run() {
        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                listener.onFailure(e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response){
               listener.onResponse(response);
            }
        });
    }

}
