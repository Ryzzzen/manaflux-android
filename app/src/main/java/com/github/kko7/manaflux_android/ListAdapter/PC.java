package com.github.kko7.manaflux_android.ListAdapter;

public class PC {
    private String ipAddress;
    private String pcName;

    public PC(String ipAddress, String pcName) {
        this.ipAddress = ipAddress;
        this.pcName = pcName;
    }

    public String getAddress() {
        return ipAddress;
    }

    public void setAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getName() {
        return pcName;
    }

    public void setName(String pcName) {
        this.pcName = pcName;
    }

}