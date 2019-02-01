package com.github.kko7.manaflux_android.Connection;

import com.google.gson.annotations.SerializedName;

public class ApiData {

    @SerializedName("success")
    private Boolean success;
    @SerializedName("errorCode")
    private String errorCode;
    @SerializedName("success")
    private String error;
    @SerializedName("summonerName")
    private String summonerName;
    @SerializedName("summonerLevel")
    private Integer summonerLevel;
    @SerializedName("positions")
    private String[] positions;
    @SerializedName("position")
    private String position;

    public ApiData(Boolean success, String errorCode, String error, String summonerName, Integer summonerLevel, String[] positions, String position) {
        this.success = success;
        this.errorCode = errorCode;
        this.error = error;
        this.summonerName = summonerName;
        this.summonerLevel = summonerLevel;
        this.positions = positions;
        this.position = position;
    }

    public Boolean getSuccess() {
        return success;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getError() {
        return error;
    }

    public String getSummonerName() {
        return summonerName;
    }

    public Integer getSummonerLevel() {
        return summonerLevel;
    }

    public String[] getPositions() {
        return positions;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}