package com.github.kko7.manaflux_android.Connection;

import okhttp3.Call;
import okhttp3.Response;
import java.io.IOException;

public interface HttpListener {
    void onResponse(Call call, Response response);
    void onFailure(Call call, IOException exception);
}
