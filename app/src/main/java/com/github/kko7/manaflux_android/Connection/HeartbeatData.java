package com.github.kko7.manaflux_android.Connection;

import com.google.gson.annotations.SerializedName;

public class HeartbeatData {

    @SerializedName("name")
    private String championName;
    @SerializedName("img")
    private String championImg;

    public HeartbeatData(String championName, String championImg) {
        this.championName = championName;
        this.championImg = championImg;
    }

    public String getChampionName() {
        return championName;
    }

    public String getChampionImg() {
        return championImg;
    }
}