package com.github.kko7.manaflux_android.Models;

public class Spells {
    private int id;
    private String name;
    private String path;

    public Spells(int id, String name, String path) {
        this.id = id;
        this.name = name;
        this.path = path;
    }

    public int getSpellId() {
        return id;
    }

    public String getSpellName() {
        return name;
    }

    public String getPath() {
        return path;
    }
}