package com.github.kko7.manaflux_android.Connection;

import okhttp3.Response;
import java.io.IOException;

public interface HttpListener {
    void onResponse(Response response);
    void onFailure(IOException exception);
}
