package com.github.kko7.manaflux_android.Database;

class Constants {
    static final String ROW_ID = "id";
    static final String ROW_ADDRESS = "address";
    static final String ROW_NAME = "name";

    static final String DB_NAME = "b_DB";
    static final String TB_NAME = "b_TB";
    static final int DB_VERSION = '1';

    static final String CREATE_TB = "CREATE TABLE b_TB(id INTEGER PRIMARY KEY AUTOINCREMENT,"
            + "address TEXT NOT NULL, name TEXT NOT NULL);";
}
