package com.github.kko7.manaflux_android.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DBAdapter {
    Context c;
    SQLiteDatabase db;
    DBHelper helper;

    public DBAdapter(Context ctx) {
        this.c = ctx;
        helper = new DBHelper(c);
    }

    public DBAdapter openDB() {
        try {
            db = helper.getWritableDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return this;
    }

    public void close() {
        try {
            helper.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public long add(String address, String name) {
        try {
            ContentValues cv = new ContentValues();
            cv.put(Constants.ADDRESS, address);
            cv.put(Constants.NAME, name);

            return db.insert(Constants.TB_NAME, Constants.ROW_ID, cv);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public Cursor getAllDevices() {
        String[] columns = {Constants.ROW_ID, Constants.ADDRESS, Constants.NAME};

        return db.query(Constants.TB_NAME, columns, null, null, null, null, null);
    }

    public long UPDATE(int id, String address, String name) {
        try {
            ContentValues cv = new ContentValues();
            cv.put(Constants.ADDRESS, address);
            cv.put(Constants.NAME, name);

            return db.update(Constants.TB_NAME, cv, Constants.ROW_ID + " =?", new String[]{String.valueOf(id)});

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public long Delete(int id) {
        try {
            return db.delete(Constants.TB_NAME, Constants.ROW_ID + " =?", new String[]{String.valueOf(id)});
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

}
