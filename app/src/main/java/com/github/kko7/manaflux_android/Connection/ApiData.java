package com.github.kko7.manaflux_android.Connection;

import com.google.gson.annotations.SerializedName;

public class ApiData {

    @SerializedName("success")
    private Boolean success;
    @SerializedName("error")
    private String error;
    @SerializedName("errorCode")
    private String errorCode;
    @SerializedName("authentified")
    private Boolean authentified;
    @SerializedName("summonerName")
    private String summonerName;
    @SerializedName("summonerLevel")
    private Integer summonerLevel;
    @SerializedName("positions")
    private String[] positions;

    public ApiData(Boolean success, String error, String errorCode, Boolean authentified, String summonerName, Integer summonerLevel, String[] positions) {
        this.success = success;
        this.error = error;
        this.errorCode = errorCode;
        this.authentified = authentified;
        this.summonerName = summonerName;
        this.summonerLevel = summonerLevel;
        this.positions = positions;
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
}