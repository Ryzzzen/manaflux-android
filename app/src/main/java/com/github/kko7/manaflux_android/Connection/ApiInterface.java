package com.github.kko7.manaflux_android.Connection;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiInterface {

    @POST("/api/v1/authentify/Android/{deviceName}")
    Call<ApiData> authentifyDevice(@Path("deviceName") String deviceName);

    @GET("/api/v1/heartbeat")
    Call<HeartbeatData> getHeartbeat();

    @GET("/api/v1/summoner")
    Call<ApiData> getSummoner();

    @GET("/api/v1/actions/positions")
    Call<ApiData> getPositions();

    @POST("/api/v1/actions/positions/{position}")
    Call<ApiData> setPosition(@Path("position") Integer position);
}