package com.github.kko7.manaflux_android.Database;

public class Device {
    private final String name;
    private final String address;
    private final Integer id;

    public Device(String name, String address, Integer id) {
        this.address = address;
        this.name = name;
        this.id = id;
    }

    String getAddress() {
        return address;
    }

    String getName() {
        return name;
    }

    Integer getId() {
        return id;
    }
}
