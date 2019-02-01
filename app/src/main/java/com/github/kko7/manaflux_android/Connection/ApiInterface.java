package com.github.kko7.manaflux_android.Connection;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiInterface {

    @POST("/api/v1/authentify/Android/{deviceName}")
    Call<ApiData> authentifyDevice(@Path("deviceName") String deviceName);

    @GET("/api/v1/heartbeat")
    Call<ApiData> getHeartbeat();

    @GET("/api/v1/summoner")
    Call<ApiData> getSummoner();

    @GET("/api/v1/actions/positions")
    Call<ApiData> getPositions();

    @GET("/api/v1/actions/current-champion")
    Call<ApiData> getCurrentChampion();

    @POST("/api/v1/actions/positions/{position}")
    Call<ApiData> setPosition(@Path("position") Integer position);

    @POST("/api/v1/actions/summoner-spells/load")
    Call<ApiData> getSpells();

    @POST("/api/v1/actions/runes/load")
    Call<ApiData> getRunes();

}