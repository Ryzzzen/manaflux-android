package com.github.kko7.manaflux_android.Models;

import com.google.gson.annotations.SerializedName;

public class HeartbeatData {

    @SerializedName("inChampionSelect")
    private Boolean inChampionSelect;
    @SerializedName("name")
    private String championName;
    @SerializedName("img")
    private String championImg;

    public HeartbeatData(Boolean inChampionSelect, String championName, String championImg) {
        this.inChampionSelect = inChampionSelect;
        this.championName = championName;
        this.championImg = championImg;
    }

    public Boolean getInChampionSelect() {
        return inChampionSelect;
    }

    public String getChampionName() {
        return championName;
    }

    public String getChampionImg() {
        return championImg;
    }
}