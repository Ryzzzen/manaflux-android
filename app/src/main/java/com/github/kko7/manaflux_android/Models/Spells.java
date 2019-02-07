package com.github.kko7.manaflux_android.Models;


public class Spells {
    int id;
    String name;
    String path;

    public int getSpellId() {
        return id;
    }

    public String getSpellName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public Spells(int id, String name, String path) {
        this.id = id;
        this.name = name;
        this.path = path;
    }
}