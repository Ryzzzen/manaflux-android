package com.github.kko7.manaflux_android.Models;

public class Device {
    private final String name;
    private final String address;
    private final Integer id;

    public Device(String name, String address, Integer id) {
        this.address = address;
        this.name = name;
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public String getName() {
        return name;
    }

    public Integer getId() {
        return id;
    }
}
