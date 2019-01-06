package com.github.kko7.manaflux_android.UserInterface;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.kko7.manaflux_android.Connection.HttpGet;
import com.github.kko7.manaflux_android.Connection.HttpListener;
import com.github.kko7.manaflux_android.Helpers.PrefsHelper;
import com.github.kko7.manaflux_android.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import okhttp3.Response;
import okhttp3.ResponseBody;

public class LoadActivity extends AppCompatActivity implements HttpListener {

    private static final String TAG = "LoadActivity";
    PrefsHelper prefsHelper;
    RelativeLayout layout;
    TextView status;
    String ip, name, token;

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
        ip = prefsHelper.getString("deviceIP");
        name = prefsHelper.getString("deviceNAME");
        token = prefsHelper.getString("token");
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    private void setBackground() {
        String value = (prefsHelper.getBackground("background"));
        int id = getResources().getIdentifier(value + "_bg", "mipmap", getPackageName());
        layout.setBackgroundResource(id);
    }

    private void check() {
        try {
            HttpGet httpGet = new HttpGet("http://" + ip + ":4500/summoner", this);
            httpGet.run();
        } catch (Exception e) {
            status.setText(String.valueOf(e));
            e.printStackTrace();
            Log.d(TAG, e.getMessage());
        }
    }

    @Override
    public void onResponse(final Response response) {
        try {
            if (!response.isSuccessful()) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        status.setText(response.toString());
                        status.setVisibility(TextView.VISIBLE);
                    }
                });
            } else {
                JSONObject data = new JSONObject(Objects.requireNonNull(response.body()).string());
                prefsHelper.saveString("summonerName", data.getString("summonerName"));
                prefsHelper.saveString("summonerLevel", data.getString("summonerLevel"));
            }
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(final String exception) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                status.setText(exception);
            }
        });
    }
}