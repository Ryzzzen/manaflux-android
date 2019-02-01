package com.github.kko7.manaflux_android.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DBAdapter {
    private SQLiteDatabase db;
    private final DBHelper helper;

    public DBAdapter(Context context) {
        helper = new DBHelper(context);
    }

    public void openDB() {
        try {
            db = helper.getWritableDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void close() {
        try {
            helper.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public Cursor getAllDevices() {
        String[] columns = {Constants.ROW_ID, Constants.ROW_ADDRESS, Constants.ROW_NAME};
        return db.query(Constants.TB_NAME, columns, null, null, null, null, null);
    }

    public long ADD(String address, String name) {
        try {
            ContentValues cv = new ContentValues();
            cv.put(Constants.ROW_ADDRESS, address);
            cv.put(Constants.ROW_NAME, name);
            return db.insert(Constants.TB_NAME, Constants.ROW_ID, cv);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public long UPDATE(int id, String address, String name) {
        try {
            ContentValues cv = new ContentValues();
            cv.put(Constants.ROW_ADDRESS, address);
            cv.put(Constants.ROW_NAME, name);
            return db.update(Constants.TB_NAME, cv, Constants.ROW_ID + " =?", new String[]{String.valueOf(id)});
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public long DELETE(int id) {
        try {
            return db.delete(Constants.TB_NAME, Constants.ROW_ID + " =?", new String[]{String.valueOf(id)});
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

}
