package com.github.kko7.manaflux_android.Connection;

import com.google.gson.annotations.SerializedName;

public class HeartbeatData {

    @SerializedName("success")
    private Boolean success;
    @SerializedName("mame")
    private String championName;
    @SerializedName("id")
    private String championId;
    @SerializedName("img")
    private String championImg;

    public HeartbeatData(Boolean success, String championName, String championId, String championImg) {
        this.success = success;
        this.championName = championName;
        this.championId = championId;
        this.championImg = championImg;
    }

    public Boolean getSuccess() {
        return success;
    }

    public String getChampionName() {
        return championName;
    }

    public String getChampionId() {
        return championId;
    }

    public String getChampionImg() {
        return championImg;
    }
}