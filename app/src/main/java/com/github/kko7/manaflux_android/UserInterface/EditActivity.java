package com.github.kko7.manaflux_android.UserInterface;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.github.kko7.manaflux_android.Helpers.DatabaseHelper;
import com.github.kko7.manaflux_android.Helpers.PrefsHelper;
import com.github.kko7.manaflux_android.MainActivity;
import com.github.kko7.manaflux_android.R;

import java.util.Objects;

public class EditActivity extends AppCompatActivity {


    private static final String TAG = EditActivity.class.getSimpleName();
    String address, name;
    int id;
    Intent intent;
    EditText addressTxt, nameTxt;
    ImageButton closeButton, saveButton;
    Button deleteButton, selectButton;
    RelativeLayout layout;
    PrefsHelper prefsHelper;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        Log.d(TAG, "onCreate: Started.");

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        prefsHelper = PrefsHelper.getInstance(this);
        dbHelper = new DatabaseHelper(this);
        saveButton = findViewById(R.id.save_button);
        closeButton = findViewById(R.id.close_button);
        deleteButton = findViewById(R.id.delete_button);
        selectButton = findViewById(R.id.select_button);
        addressTxt = findViewById(R.id.address_edit);
        nameTxt = findViewById(R.id.name_edit);
        layout = findViewById(R.id.edit_layout);
        intent = getIntent();
        address = Objects.requireNonNull(intent.getExtras()).getString("ADDRESS");
        name = Objects.requireNonNull(Objects.requireNonNull(intent).getExtras()).getString("NAME");
        id = intent.getExtras().getInt("ID");
        addressTxt.setText(address);
        nameTxt.setText(name);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper.update(id, nameTxt.getText().toString(), addressTxt.getText().toString());
                finish();
            }
        });
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper.delete(id);
                finish();
            }
        });
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prefsHelper.saveString("device-ip", address);
                prefsHelper.saveString("device-name", name);
                startActivity(new Intent(EditActivity.this, MainActivity.class));
            }
        });
        setBackground();
    }

    private void setBackground() {
        String value = prefsHelper.getBackground("background");
        int id = getResources().getIdentifier(value + "_bg", "mipmap", getPackageName());
        layout.setBackgroundResource(id);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setBackground();
    }
}