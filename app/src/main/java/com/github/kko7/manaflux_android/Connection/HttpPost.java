package com.github.kko7.manaflux_android.Connection;

import java.io.IOException;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpPost {
    private static final MediaType MEDIA_TYPE_PLAIN = MediaType.get("text/plain; charset=utf-8");
    private final OkHttpClient client = new OkHttpClient();
    private final String url;
    private final String token;

    public HttpPost(String url, String token) {
        this.url = url;
        this.token = token;
    }

    public void run() throws Exception {

        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(MEDIA_TYPE_PLAIN, token))
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new Exception("Unexpected code " + response);

            System.out.println(Objects.requireNonNull(response.body()).string());
        }
    }
}