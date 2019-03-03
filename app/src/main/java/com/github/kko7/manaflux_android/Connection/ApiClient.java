package com.github.kko7.manaflux_android.Connection;

import android.content.Context;
import android.support.annotation.NonNull;

import com.github.kko7.manaflux_android.Helpers.PrefsHelper;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private final Context context;

    public ApiClient(Context context) {
        this.context = context;
    }

    public ApiInterface getClient() {

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        httpClient.addInterceptor(new Interceptor() {
            @NonNull
            @Override
            public Response intercept(@NonNull Interceptor.Chain chain) throws IOException {
                Request request = chain.request()
                        .newBuilder()
                        .addHeader("Authorization", PrefsHelper.getInstance(context).getString("auth-token"))
                        .build();
                return chain.proceed(request);
            }
        });

        return new Retrofit.Builder()
                .baseUrl(PrefsHelper.getInstance(context).getString("device-ip") + "4500")
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build()
                .create(ApiInterface.class);
    }
}
