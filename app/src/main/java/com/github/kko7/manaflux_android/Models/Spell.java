package com.github.kko7.manaflux_android.Models;

import com.google.gson.annotations.SerializedName;

public class Spell {

    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("path")
    private String path;

    public Spell(int id, String name, String path) {
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