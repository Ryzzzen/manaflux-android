package com.github.kko7.manaflux_android.Database.Device;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DeviceHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 4;

    private static final String DATABASE_NAME = "devices.db";

    public DeviceHandler(Context context ) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_TABLE_DEVICE = "CREATE TABLE " + Device.TABLE  + "("
                + Device.KEY_ID  + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + Device.KEY_address + " TEXT, "
                + Device.KEY_name + " TEXT )";

        db.execSQL(CREATE_TABLE_DEVICE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Device.TABLE);

        onCreate(db);

    }

}