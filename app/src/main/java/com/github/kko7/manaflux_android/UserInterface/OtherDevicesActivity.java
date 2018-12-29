package com.github.kko7.manaflux_android.UserInterface;

import android.app.Dialog;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.github.kko7.manaflux_android.Database.Adapter;
import com.github.kko7.manaflux_android.Database.DBAdapter;
import com.github.kko7.manaflux_android.Database.Device;
import com.github.kko7.manaflux_android.Helpers.PrefsHelper;
import com.github.kko7.manaflux_android.R;

import java.util.ArrayList;

public class OtherDevicesActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    ArrayList<Device> devices = new ArrayList<>();
    Button addButton, saveBtn, retrieveBtn;
    EditText nameEditTxt, addressEditTxt;
    RecyclerView mRecyclerView;
    RelativeLayout layout;
    PrefsHelper prefsHelper;
    Adapter adapter;
    Dialog d;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
        Log.d(TAG, "onCreate: Started.");
        initViews();
        initList();
        setBackground();
    }

    protected void initList() {
        adapter = new Adapter(this, devices);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        retrieve();
    }

    protected void initViews() {
        prefsHelper = PrefsHelper.getInstance(this);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mRecyclerView = findViewById(R.id.lanList);
        layout = findViewById(R.id.select_layout);

    }

    private void showDialog() {
        d = new Dialog(this);
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.setContentView(R.layout.dialog_layout);

        addressEditTxt = d.findViewById(R.id.addressEditTxt);
        nameEditTxt = d.findViewById(R.id.nameEditTxt);
        saveBtn = d.findViewById(R.id.saveBtn);
        retrieveBtn = d.findViewById(R.id.retrieveBtn);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save(nameEditTxt.getText().toString(), addressEditTxt.getText().toString());
            }
        });

        retrieveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                retrieve();
            }
        });

        d.show();
    }

    private void retrieve() {
        DBAdapter db = new DBAdapter(this);
        db.openDB();
        devices.clear();
        Cursor c = db.getAllDevices();

        while (c.moveToNext()) {
            int id = c.getInt(0);
            String address = c.getString(1);
            String name = c.getString(2);

            Device p = new Device(address, name, id);

            devices.add(p);
        }

        if (!(devices.size() < 1)) {
            mRecyclerView.setAdapter(adapter);
        }

        db.close();

    }

    private void save(String address, String name) {

        DBAdapter db = new DBAdapter(this);
        db.openDB();
        long result = db.ADD(address, name);

        if (result > 0) {
            addressEditTxt.setText("");
            nameEditTxt.setText("");
        } else {
            Toast.makeText(getApplicationContext(), "Unable To Insert", Toast.LENGTH_SHORT).show();
        }

        db.close();
        retrieve();
    }

    protected void setBackground() {
        String value = prefsHelper.getBackground("background");
        int id = getResources().getIdentifier(value + "_bg", "mipmap", getPackageName());
        layout.setBackgroundResource(id);
    }

    @Override
    protected void onResume() {
        super.onResume();
        retrieve();
        setBackground();
    }


}
