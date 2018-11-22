package com.github.kko7.manaflux_android;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.github.kko7.manaflux_android.ActivityHandler.AddActivity;
import com.github.kko7.manaflux_android.ActivityHandler.EditActivity;
import com.github.kko7.manaflux_android.ActivityHandler.SettingsActivity;
import com.github.kko7.manaflux_android.Database.DBHandler;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    Button editButton;
    Button connectButton;
    Button settingsButton;
    Button addButton;
    String selectedID;
    ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editButton = findViewById(R.id.edit_button);
        connectButton = findViewById(R.id.connect_button);
        addButton = findViewById(R.id.add_button);
        settingsButton = findViewById(R.id.settings_button);
        mListView = findViewById(R.id.listView);
        Log.d(TAG, "onCreate: Started.");
        initViews();
    }

    protected void initButtons() {

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, EditActivity.class).putExtra("deviceID", selectedID));
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AddActivity.class));
            }
        });
    }

    protected void checkPerms() {
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            initButtons();
        } else {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
            initButtons();
        }
    }
    
    protected void initList() {

        DBHandler db = new DBHandler(this);
        ArrayList<HashMap<String, String>> devicesList = db.GetDevices();
        ListAdapter listAdapter = new SimpleAdapter(MainActivity.this, devicesList, R.layout.list_adapter, new String[]{"address", "name"}, new int[]{R.id.ip_text, R.id.name_text});
        mListView.setAdapter(listAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,long arg3) {
                view.setSelected(true);
                selectedID = String.valueOf(arg3);
                editButton.setVisibility(View.VISIBLE);
                connectButton.setVisibility(View.VISIBLE);
                addButton.setVisibility(View.GONE);
            }
        });

        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View view, int pos, long id) {
                view.setSelected(false);
                editButton.setVisibility(View.GONE);
                connectButton.setVisibility(View.GONE);
                addButton.setVisibility(View.VISIBLE);
                return true;
            }
        });
    }

    protected void initViews() {
        editButton.setVisibility(View.GONE);
        mListView.setVisibility(View.VISIBLE);
        connectButton.setVisibility(View.GONE);
        addButton.setVisibility(View.VISIBLE);
        checkPerms();
        initList();
    }
}
