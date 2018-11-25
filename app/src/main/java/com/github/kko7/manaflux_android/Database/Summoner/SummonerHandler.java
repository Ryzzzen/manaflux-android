package com.github.kko7.manaflux_android.Database.Summoner;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SummonerHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "summoners.db";

    public SummonerHandler(Context context ) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_TABLE_SUMMONER = "CREATE TABLE " + Summoner.TABLE  + "("
                + Summoner.KEY_ID  + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + Summoner.KEY_name + " TEXT, "
                + Summoner.KEY_level + " TEXT )";

        db.execSQL(CREATE_TABLE_SUMMONER);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Summoner.TABLE);

        onCreate(db);

    }

}