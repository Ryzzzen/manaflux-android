package com.github.kko7.manaflux_android.UserInterface;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.kko7.manaflux_android.Connection.HttpGet;
import com.github.kko7.manaflux_android.Helpers.PrefsHelper;
import com.github.kko7.manaflux_android.R;

public class LoadActivity extends AppCompatActivity {

    private static final String TAG = "LoadActivity";
    PrefsHelper prefsHelper;
    RelativeLayout layout;
    TextView status;
    String ip, name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);
        Log.d(TAG, "onCreate: Started.");
        init();
        setBackground();
        check();
    }

    private void init() {
        prefsHelper = PrefsHelper.getInstance(this);
        layout = findViewById(R.id.load_layout);
        status = findViewById(R.id.status_text);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    private void setBackground() {
        String value = (prefsHelper.getBackground("background"));
        int id = getResources().getIdentifier(value + "_bg", "mipmap", getPackageName());
        layout.setBackgroundResource(id);
    }

    private void check(){

        try {
            ip = prefsHelper.getString("deviceIP");
            name = prefsHelper.getString("deviceNAME");
        } catch (Exception e) {
            status.setText(String.valueOf(e));
            e.printStackTrace();
            Log.d(TAG, e.getMessage());
        }

        try {
            HttpGet httpGet = new HttpGet("http://" + ip + ":4500/summoner");
            httpGet.run();
        } catch (Exception e) {
            status.setText(String.valueOf(e));
            e.printStackTrace();
            Log.d(TAG, e.getMessage());
        }

    }

}