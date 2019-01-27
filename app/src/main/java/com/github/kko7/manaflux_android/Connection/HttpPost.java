package com.github.kko7.manaflux_android.Connection;

import android.content.Context;

import com.github.kko7.manaflux_android.Helpers.PrefsHelper;

import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpPost {
    private static final MediaType MEDIA_TYPE_PLAIN = MediaType.get("text/plain; charset=utf-8");
    private String url;
    private String body;
    private Context context;

    public HttpPost(String url, String body, Context context) {
        this.url = url;
        this.body = body;
        this.context = context;
    }

    public void run(int connectTimeout, int readTimeout) throws Exception {
        connectTimeout = connectTimeout != 0 ? connectTimeout : 10;
        readTimeout = readTimeout != 0 ? readTimeout : 10;

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(connectTimeout, TimeUnit.SECONDS)
                .readTimeout(readTimeout, TimeUnit.SECONDS)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(MEDIA_TYPE_PLAIN, body))
                .addHeader("authorization", PrefsHelper.getInstance(context).getString("auth-token"))
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new Exception("Unexpected code " + response);
        }
    }
}