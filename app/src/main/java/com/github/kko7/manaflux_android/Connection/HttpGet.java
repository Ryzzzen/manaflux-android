package com.github.kko7.manaflux_android.Connection;

import android.content.Context;
import android.support.annotation.NonNull;

import com.github.kko7.manaflux_android.Helpers.PrefsHelper;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public final class HttpGet {
    private final String url;
    private HttpListener listener;
    private Context context;

    public HttpGet(String url, HttpListener httpListener, Context context) {
        this.url = url;
        this.listener = httpListener;
        this.context = context;
    }

    public void run(int connectTimeout, int readTimeout) {
        connectTimeout = connectTimeout != 0 ? connectTimeout : 10;
        readTimeout = readTimeout != 0 ? readTimeout : 10;

        Request request = new Request.Builder()
                .addHeader("authorization", "Basic " + PrefsHelper.getInstance(context).getString("auth-token"))
                .url(url)
                .build();

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(connectTimeout, TimeUnit.SECONDS)
                .readTimeout(readTimeout, TimeUnit.SECONDS)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException exception) {
                listener.onFailure(call, exception);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                listener.onResponse(call, response);
            }
        });
    }
}
