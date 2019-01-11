package com.github.kko7.manaflux_android.UserInterface;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.github.kko7.manaflux_android.Database.DBAdapter;
import com.github.kko7.manaflux_android.Helpers.PrefsHelper;
import com.github.kko7.manaflux_android.MainActivity;
import com.github.kko7.manaflux_android.R;

import java.util.Objects;

public class EditActivity extends AppCompatActivity {

    Intent i;
    String address;
    String name;
    int id;

    EditText addressTxt, nameTxt;
    ImageButton closeBtn, saveBtn;
    Button deleteBtn, selectBtn;
    LinearLayout layout;
    PrefsHelper prefsHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        init();
        initButtons();
        initOther();
        setBackground();
    }

    private void init() {
        i = getIntent();
        prefsHelper = PrefsHelper.getInstance(this);
        saveBtn = findViewById(R.id.save_button);
        closeBtn = findViewById(R.id.close_button);
        deleteBtn = findViewById(R.id.delete_button);
        selectBtn = findViewById(R.id.select_button);
        addressTxt = findViewById(R.id.address_edit);
        nameTxt = findViewById(R.id.name_edit);
        layout = findViewById(R.id.edit_layout);
        address = Objects.requireNonNull(i.getExtras()).getString("ADDRESS");
        name = Objects.requireNonNull(Objects.requireNonNull(i).getExtras()).getString("NAME");
        id = i.getExtras().getInt("ID");
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    private void initButtons() {

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update(id, nameTxt.getText().toString(), addressTxt.getText().toString());
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete(id);
            }
        });

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        selectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prefsHelper.saveString("deviceIP", address);
                prefsHelper.saveString("deviceNAME", name);
                startActivity(new Intent(EditActivity.this, MainActivity.class));
            }
        });
    }

    private void initOther() {
        addressTxt.setText(address);
        nameTxt.setText(name);
    }

     void update(int id, String newAddress, String newName) {
        DBAdapter db = new DBAdapter(this);
        db.openDB();
        long result = db.UPDATE(id, newAddress, newName);

        if (result > 0) {
            Toast.makeText(getApplicationContext(), getString(R.string.db_success), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.db_fail), Toast.LENGTH_SHORT).show();
        }

        db.close();
    }

    void delete(int id) {
        DBAdapter db = new DBAdapter(this);
        db.openDB();
        long result = db.DELETE(id);

        if (result >= 0) {
            finish();
        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.db_fail), Toast.LENGTH_SHORT).show();
        }

        db.close();
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