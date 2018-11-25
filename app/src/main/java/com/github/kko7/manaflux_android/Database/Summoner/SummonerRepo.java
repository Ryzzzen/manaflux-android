package com.github.kko7.manaflux_android.Database.Summoner;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class SummonerRepo {
    private SummonerHandler dbHandler;

    public SummonerRepo(Context context) {
        dbHandler = new SummonerHandler(context);
    }

    public int insert(Summoner summoner) {

        SQLiteDatabase db = dbHandler.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Summoner.KEY_name, summoner.name);
        values.put(Summoner.KEY_level, summoner.level);

        long summoner_Id = db.insert(Summoner.TABLE, null, values);
        db.close();
        return (int) summoner_Id;
    }

    public void delete(int summoner_Id) {

        SQLiteDatabase db = dbHandler.getWritableDatabase();
        db.delete(Summoner.TABLE, Summoner.KEY_ID + " = ?", new String[] { String.valueOf(summoner_Id) });
        db.close();
    }

    public void update(Summoner summoner) {

        SQLiteDatabase db = dbHandler.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(Summoner.KEY_name, summoner.name);
        values.put(Summoner.KEY_level, summoner.level);

        db.update(Summoner.TABLE, values, Summoner.KEY_ID + " = ?", new String[] { String.valueOf(summoner.summoner_ID) });
        db.close();
    }

    public ArrayList<HashMap<String, String>> getSummonersList() {
        SQLiteDatabase db = dbHandler.getReadableDatabase();
        String selectQuery =  "SELECT  " +
                Summoner.KEY_ID + "," +
                Summoner.KEY_name + "," +
                Summoner.KEY_level +
                " FROM " + Summoner.TABLE;

        ArrayList<HashMap<String, String>> summonersList = new ArrayList<>();

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> summoner = new HashMap<>();
                summoner.put("id", cursor.getString(cursor.getColumnIndex(Summoner.KEY_ID)));
                summoner.put("name", cursor.getString(cursor.getColumnIndex(Summoner.KEY_name)));
                summoner.put("level", cursor.getString(cursor.getColumnIndex(Summoner.KEY_level)));
                summonersList.add(summoner);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return summonersList;

    }

    public Summoner getSummonerById(int Id){
        SQLiteDatabase db = dbHandler.getReadableDatabase();
        String selectQuery =  "SELECT  " +
                Summoner.KEY_ID + "," +
                Summoner.KEY_name + "," +
                Summoner.KEY_level +
                " FROM " + Summoner.TABLE
                + " WHERE " +
                Summoner.KEY_ID + "=?";

        // int iCount =0;
        Summoner summoner = new Summoner();

        Cursor cursor = db.rawQuery(selectQuery, new String[] { String.valueOf(Id) } );

        if (cursor.moveToFirst()) {
            do {
                summoner.summoner_ID = cursor.getInt(cursor.getColumnIndex(Summoner.KEY_ID));
                summoner.name = cursor.getString(cursor.getColumnIndex(Summoner.KEY_name));
                summoner.level = cursor.getString(cursor.getColumnIndex(Summoner.KEY_level));

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return summoner;
    }

}
