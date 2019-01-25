package com.github.kko7.manaflux_android.Helpers;

import android.content.Context;
import android.widget.Toast;

import com.github.kko7.manaflux_android.Database.DBAdapter;
import com.github.kko7.manaflux_android.R;

public class DatabaseHelper {

    private Context context;

    public DatabaseHelper(Context context) {
        this.context = context;
    }

    public void update(int id, String newAddress, String newName) {
        DBAdapter db = new DBAdapter(context);
        db.openDB();
        long result = db.UPDATE(id, newAddress, newName);

        if (result > 0) {
            Toast.makeText(context, context.getString(R.string.db_success), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, context.getString(R.string.db_fail), Toast.LENGTH_SHORT).show();
        }

        db.close();
    }

    public void delete(int id) {
        DBAdapter db = new DBAdapter(context);
        db.openDB();
        long result = db.DELETE(id);

        if (!(result >= 0)) {
            Toast.makeText(context, context.getString(R.string.db_fail), Toast.LENGTH_SHORT).show();
        }

        db.close();
    }

    public void save(String address, String name) {
        DBAdapter db = new DBAdapter(context);
        db.openDB();
        long result = db.ADD(address, name);

        if (!(result > 0)) {
            Toast.makeText(context, context.getString(R.string.db_fail), Toast.LENGTH_SHORT).show();
        }

        db.close();
    }
}
