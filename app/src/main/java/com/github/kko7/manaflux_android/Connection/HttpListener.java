package com.github.kko7.manaflux_android.Connection;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

public interface HttpListener {
    void onResponse(Call call, Response response);

    void onFailure(Call call, IOException exception);
}
