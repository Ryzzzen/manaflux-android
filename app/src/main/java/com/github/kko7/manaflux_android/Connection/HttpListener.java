package com.github.kko7.manaflux_android.Connection;

import okhttp3.Response;

public interface HttpListener {
    void onResponse(Response response);
    void onFailure(String exception);
}
