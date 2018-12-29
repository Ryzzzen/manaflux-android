package com.github.kko7.manaflux_android.UserInterface;

import android.content.Intent;
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
import com.github.kko7.manaflux_android.R;

import java.util.Objects;

public class EditActivity extends AppCompatActivity {

    EditText addressTxt, nameTxt;
    ImageButton closeBtn, saveBtn;
    Button deleteBtn, selectBtn;
    LinearLayout layout;
    PrefsHelper prefsHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        prefsHelper = PrefsHelper.getInstance(this);
        layout = findViewById(R.id.edit_layout);

        Intent i = getIntent();
        final String address = Objects.requireNonNull(i.getExtras()).getString("ADDRESS");
        final String name = i.getExtras().getString("NAME");
        final int id = i.getExtras().getInt("ID");

        saveBtn = findViewById(R.id.save_button);
        closeBtn = findViewById(R.id.close_button);
        deleteBtn = findViewById(R.id.delete_button);
        selectBtn = findViewById(R.id.select_button);
        addressTxt = findViewById(R.id.address_edit);
        nameTxt = findViewById(R.id.name_edit);

        addressTxt.setText(address);
        nameTxt.setText(name);

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
        setBackground();

        selectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prefsHelper.saveString("deviceIP", address);
                prefsHelper.saveString("deviceNAME", name);
                startActivity(new Intent(EditActivity.this, DashboardActivity.class));
            }
        });
        setBackground();
    }

    private void update(int id, String newAddress, String newName) {
        DBAdapter db = new DBAdapter(this);
        db.openDB();
        long result = db.UPDATE(id, newAddress, newName);

        if (result > 0) {
            Toast.makeText(getApplicationContext(), "Updated Successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "Unable to Update", Toast.LENGTH_SHORT).show();
        }

        db.close();
        finish();
    }

    private void delete(int id) {
        DBAdapter db = new DBAdapter(this);
        db.openDB();
        long result = db.DELETE(id);

        if (result >= 0) {
            finish();
        } else {
            Toast.makeText(getApplicationContext(), "Unable to Update", Toast.LENGTH_SHORT).show();
        }

        db.close();
    }

    protected void setBackground() {
        String value = prefsHelper.getBackground("background");
        int id = getResources().getIdentifier(value + "_bg", "mipmap", getPackageName());
        layout.setBackgroundResource(id);
    }
}