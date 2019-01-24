package com.github.kko7.manaflux_android.Connection;

import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpPost {
    private static final MediaType MEDIA_TYPE_PLAIN = MediaType.get("text/plain; charset=utf-8");
    private String url;
    private String token, authToken;

    public HttpPost(String url, String token, String authToken) {
        this.url = url;
        this.token = token;
        this.authToken = authToken;
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
                .post(RequestBody.create(MEDIA_TYPE_PLAIN, token))
                .addHeader("authorization", authToken)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new Exception("Unexpected code " + response);
        }
    }
}