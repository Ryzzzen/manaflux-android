package com.github.kko7.manaflux_android.UserInterface;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.kko7.manaflux_android.Connection.HttpGet;
import com.github.kko7.manaflux_android.Connection.HttpListener;
import com.github.kko7.manaflux_android.Connection.HttpPost;
import com.github.kko7.manaflux_android.CustomElements.GifView;
import com.github.kko7.manaflux_android.Helpers.PrefsHelper;
import com.github.kko7.manaflux_android.R;

import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Response;

public class LoadActivity extends AppCompatActivity implements HttpListener {

    private static final String TAG = "LoadActivity";
    PrefsHelper prefsHelper;
    RelativeLayout layout, error_layout;
    TextView error_line1, error_line2, details;
    Button refreshButton, detailsButton;
    String ip, name, token;
    Dialog d;
    HttpPost httpPost;
    GifView loading_gif;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);
        Log.d(TAG, "onCreate: Started.");
        init();
        setBackground();
        checkAll();
    }

    private void init() {
        prefsHelper = PrefsHelper.getInstance(this);
        layout = findViewById(R.id.load_layout);
        error_layout = findViewById(R.id.error_layout);
        error_line1 = findViewById(R.id.text_error1);
        error_line2 = findViewById(R.id.text_error2);
        refreshButton = findViewById(R.id.refresh_button);
        detailsButton = findViewById(R.id.details_button);
        loading_gif = findViewById(R.id.loading_gif);
        loading_gif.startGif(R.mipmap.loading);
        ip = prefsHelper.getString("deviceIP");
        name = prefsHelper.getString("deviceNAME");
        token = prefsHelper.getString("token");
        httpPost = new HttpPost("http://" + ip + ":4500/phone-token", token);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                checkAll();
            }
        });
    }

    private void checkAll() {
        loading_gif.setVisibility(View.VISIBLE);
        error_layout.setVisibility(View.GONE);
        try {
            HttpGet httpGet = new HttpGet("http://" + ip + ":4500/summoner", this);
            httpGet.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("SetTextI18n")
    private void setError(String message1, String message2){
        error_layout.setVisibility(View.VISIBLE);
        loading_gif.setVisibility(View.GONE);
        error_line1.setText(message1);
        error_line2.setText(message2);
    }

    private void setBackground() {
        String value = (prefsHelper.getBackground("background"));
        int id = getResources().getIdentifier(value + "_bg", "mipmap", getPackageName());
        layout.setBackgroundResource(id);
    }

    @Override
    public void onResponse(final Call call, final Response response) {
        try {
            if (!response.isSuccessful()) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setError("Message: " + response.message(), "Code: " + String.valueOf(response.code()));
                    }
                });
            } else {
                httpPost.run();
                JSONObject data = new JSONObject(Objects.requireNonNull(response.body()).string());
                prefsHelper.saveString("summonerName", data.getString("summonerName"));
                prefsHelper.saveString("summonerLevel", data.getString("summonerLevel"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(final Call call, final IOException exception) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        exception.printStackTrace(pw);
        final String sStackTrace = sw.toString();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String error1, error2;
                if(exception instanceof SocketTimeoutException){
                    error1 = "Connect timed out";
                    error2 = "";
                } else if(exception instanceof UnknownHostException) {
                    error1 = "Unable to resolve host";
                    error2 = "Host: " + call.request().url().host();
                } else {
                    error1 = "Other exception";
                    error2 = "Contact developer";
                }
                setError(error1, error2);

                detailsButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        showDetails(sStackTrace);
                    }
                });
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void showDetails(String text){
        d = new Dialog(this);
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.setContentView(R.layout.error_dialog);
        details = d.findViewById(R.id.details_long);
        details.setText(text);
        d.show();
    }
}