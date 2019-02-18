package com.github.kko7.manaflux_android.Connection;

import com.github.kko7.manaflux_android.Models.ApiData;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiInterface {

    @POST("/api/v1/authentify/Android/{deviceName}")
    Call<ApiData> authentifyDevice(@Path("deviceName") String deviceName);

    @GET("/api/v1/me/summoner")
    Call<ApiData> getSummoner();

    @GET("/api/v1/me/positions")
    Call<ApiData> getPositions();

    @POST("/api/v1/me/actions/positions/{position}")
    Call<ApiData> setPosition(@Path("position") Integer position);

    @GET("/api/v1/me/summoner-spells")
    Call<ApiData> getCurrentSpells();

    @GET("/api/v1/public/summoner-spells")
    Call<ApiData> getSummonerSpells();

    @POST("/api/v1/me/actions/summoner-spells")
    Call<ApiData> setSpells(@Body RequestBody spells);
}