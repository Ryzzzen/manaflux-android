package com.github.kko7.manaflux_android.UserInterface;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.Spinner;

import com.github.kko7.manaflux_android.CustomElements.CircularImageView;
import com.github.kko7.manaflux_android.R;

public class ChampionSelectActivity extends AppCompatActivity {

    private static final String TAG = ChampionSelectActivity.class.getSimpleName();
    CircularImageView championImage;
    Button spellButton1, spellButton2;
    Spinner runes;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_champion_select);
        Log.d(TAG, "onCreate: Started");

        championImage = findViewById(R.id.champion_image);
        spellButton1 = findViewById(R.id.spell_button1);
        spellButton2 = findViewById(R.id.spell_button2);
        runes = findViewById(R.id.runes_spinner);
    }
}
