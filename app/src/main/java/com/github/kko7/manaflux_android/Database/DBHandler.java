package com.github.kko7.manaflux_android.Database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class DBHandler extends SQLiteOpenHelper {
    private static final int DB_VERSION = 2;
    private static final String DB_NAME = "devices.db";
    private static final String TABLE_Devices = "device_details";

    private static final String KEY_ID = "id";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_NAME = "name";

    public DBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_Devices + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_ADDRESS + " TEXT,"
                + KEY_NAME+ " TEXT " + ")";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Devices);
        onCreate(db);
    }

    // **** CRUD (Create, Read, Update, Delete) Operations ***** //

    public void insertDeviceDetails(String address, String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cValues = new ContentValues();
        cValues.put(KEY_ADDRESS, address);
        cValues.put(KEY_NAME, name);
        long newRowId = db.insert(TABLE_Devices, null, cValues);
        db.close();
    }

    public ArrayList<HashMap<String, String>> GetDevices() {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<HashMap<String, String>> devicesList = new ArrayList<>();
        String query = "SELECT address, name FROM " + TABLE_Devices;
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            HashMap<String, String> device = new HashMap<>();
            device.put("address", cursor.getString(cursor.getColumnIndex(KEY_ADDRESS)));
            device.put("name", cursor.getString(cursor.getColumnIndex(KEY_NAME)));
            devicesList.add(device);
        }
        return devicesList;
    }

    public ArrayList<HashMap<String, String>> GetDeviceById(int deviceId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<HashMap<String, String>> devicesList = new ArrayList<>();
        String query = "SELECT address, name FROM " + TABLE_Devices;
        @SuppressLint("Recycle") Cursor cursor = db.query(TABLE_Devices, new String[]{KEY_ADDRESS}, KEY_ID + "=?", new String[]{String.valueOf(deviceId)}, null, null, null, null);
        if (cursor.moveToNext()) {
            HashMap<String, String> device = new HashMap<>();
            device.put("address", cursor.getString(cursor.getColumnIndex(KEY_ADDRESS)));
            device.put("name", cursor.getString(cursor.getColumnIndex(KEY_NAME)));
            devicesList.add(device);
        }
        return devicesList;
    }

    public void DeleteDevice(int deviceId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_Devices, KEY_ID + " = ?", new String[]{String.valueOf(deviceId)});
        db.close();
    }

    public int UpdateDeviceDetails(int id, String address, String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cValues = new ContentValues();
        cValues.put(KEY_ADDRESS, address);
        cValues.put(KEY_NAME, name);
        int count;
        count = db.update(TABLE_Devices, cValues, KEY_ID + " = ?", new String[]{String.valueOf(id)});
        return count;
    }
}