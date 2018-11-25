package com.github.kko7.manaflux_android.Database.Device;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class DeviceRepo {
    private DeviceHandler dbHandler;

    public DeviceRepo(Context context) {
        dbHandler = new DeviceHandler(context);
    }

    public int insert(Device device) {

        SQLiteDatabase db = dbHandler.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Device.KEY_address, device.address);
        values.put(Device.KEY_name, device.name);

        long device_Id = db.insert(Device.TABLE, null, values);
        db.close();
        return (int) device_Id;
    }

    public void delete(int device_Id) {

        SQLiteDatabase db = dbHandler.getWritableDatabase();
        db.delete(Device.TABLE, Device.KEY_ID + " = ?", new String[] { String.valueOf(device_Id) });
        db.close();
    }

    public void update(Device device) {

        SQLiteDatabase db = dbHandler.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(Device.KEY_address, device.address);
        values.put(Device.KEY_name, device.name);

        db.update(Device.TABLE, values, Device.KEY_ID + " = ?", new String[] { String.valueOf(device.device_ID) });
        db.close();
    }

    public ArrayList<HashMap<String, String>>  getDevicesList() {
        SQLiteDatabase db = dbHandler.getReadableDatabase();
        String selectQuery =  "SELECT  " +
                Device.KEY_ID + "," +
                Device.KEY_address + "," +
                Device.KEY_name +
                " FROM " + Device.TABLE;

        ArrayList<HashMap<String, String>> devicesList = new ArrayList<>();

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> device = new HashMap<>();
                device.put("id", cursor.getString(cursor.getColumnIndex(Device.KEY_ID)));
                device.put("address", cursor.getString(cursor.getColumnIndex(Device.KEY_address)));
                device.put("name", cursor.getString(cursor.getColumnIndex(Device.KEY_name)));
                devicesList.add(device);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return devicesList;

    }

    public Device getDeviceById(int Id){
        SQLiteDatabase db = dbHandler.getReadableDatabase();
        String selectQuery =  "SELECT  " +
                Device.KEY_ID + "," +
                Device.KEY_address + "," +
                Device.KEY_name +
                " FROM " + Device.TABLE
                + " WHERE " +
                Device.KEY_ID + "=?";

        // int iCount =0;
        Device device = new Device();

        Cursor cursor = db.rawQuery(selectQuery, new String[] { String.valueOf(Id) } );

        if (cursor.moveToFirst()) {
            do {
                device.device_ID = cursor.getInt(cursor.getColumnIndex(Device.KEY_ID));
                device.address = cursor.getString(cursor.getColumnIndex(Device.KEY_address));
                device.name = cursor.getString(cursor.getColumnIndex(Device.KEY_name));

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return device;
    }

}
