package com.github.kko7.manaflux_android.UserInterface;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.Spinner;

import com.github.kko7.manaflux_android.CustomElements.CircularImageView;
import com.github.kko7.manaflux_android.Helpers.PrefsHelper;
import com.github.kko7.manaflux_android.R;

public class ChampionSelectActivity extends AppCompatActivity {

    private static final String TAG = ChampionSelectActivity.class.getSimpleName();
    String ip;
    CircularImageView championImage;
    Button spellButton1, spellButton2;
    Spinner runesSpinner, positionsSpinner;
    PrefsHelper prefsHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_champion_select);
        Log.d(TAG, "onCreate: Started");

        prefsHelper = PrefsHelper.getInstance(this);
        championImage = findViewById(R.id.champion_image);
        spellButton1 = findViewById(R.id.spell_button1);
        spellButton2 = findViewById(R.id.spell_button2);
        runesSpinner = findViewById(R.id.runes_spinner);
        ip = prefsHelper.getString("device-ip");
    }

}
