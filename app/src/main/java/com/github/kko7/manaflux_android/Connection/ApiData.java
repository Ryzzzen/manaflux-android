package com.github.kko7.manaflux_android.Connection;

import com.google.gson.annotations.SerializedName;

public class ApiData {

    @SerializedName("success")
    private final Boolean success;
    @SerializedName("error")
    private final String error;
    @SerializedName("errorCode")
    private final String errorCode;
    @SerializedName("authentified")
    private final Boolean authentified;
    @SerializedName("summonerName")
    private final String summonerName;
    @SerializedName("summonerLevel")
    private final Integer summonerLevel;
    @SerializedName("positions")
    private final String[] positions;
    @SerializedName("position")
    private String position;

    public ApiData(Boolean success, String error, String errorCode, Boolean authentified, String summonerName, Integer summonerLevel, String[] positions, String position) {
        this.success = success;
        this.error = error;
        this.errorCode = errorCode;
        this.authentified = authentified;
        this.summonerName = summonerName;
        this.summonerLevel = summonerLevel;
        this.positions = positions;
        this.position = position;
    }

    public Boolean getSuccess() {
        return success;
    }

    public String getError() {
        return error;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public Boolean getAuthentified() {
        return authentified;
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