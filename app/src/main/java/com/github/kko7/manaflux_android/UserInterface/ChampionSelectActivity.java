package com.github.kko7.manaflux_android.UserInterface;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.kko7.manaflux_android.ChampionSelectService;
import com.github.kko7.manaflux_android.Connection.ApiClient;
import com.github.kko7.manaflux_android.Connection.ApiInterface;
import com.github.kko7.manaflux_android.CustomElements.TextView;
import com.github.kko7.manaflux_android.Helpers.PrefsHelper;
import com.github.kko7.manaflux_android.Models.ApiData;
import com.github.kko7.manaflux_android.R;
import com.github.kko7.manaflux_android.Spell;
import com.github.kko7.manaflux_android.SpellsAdapter;
import com.squareup.picasso.Picasso;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChampionSelectActivity extends AppCompatActivity {

    private static final String TAG = ChampionSelectActivity.class.getSimpleName();
    private ArrayList<Spell> mData;
    private String[] mPositions;
    private Context context;
    private TextView code;
    private TextView error;
    private ImageButton spellButton1;
    private ImageButton spellButton2;
    private RelativeLayout errorLayout;
    private RelativeLayout layout;
    private int spell1, spell2;
    private ApiInterface client;
    private String deviceIp;
    private DataReceiver dataReceiver;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_champion_select);
        Log.d(TAG, "onCreate: Started");

        context = this;
        dataReceiver = new DataReceiver();
        code = findViewById(R.id.errorCode);
        error = findViewById(R.id.error);
        layout = findViewById(R.id.select_layout);
        errorLayout = findViewById(R.id.error_layout);
        deviceIp = PrefsHelper.getInstance(this).getString("device-ip");
        startService(new Intent(context, ChampionSelectService.class));
        registerReceiver(dataReceiver, new IntentFilter("GET_CHAMPION_SELECT_DATA"));
        start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(dataReceiver);
    }

    private void start() {
        client = new ApiClient(this).getClient();
        layout.setVisibility(View.VISIBLE);
        errorLayout.setVisibility(View.GONE);
        spellButton1 = findViewById(R.id.spell_button1);
        spellButton2 = findViewById(R.id.spell_button2);

        try {
            getSummonerSpells();
            Thread.sleep(500);
            getCurrentSpells();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void updateChampion(final String name, final String img) {
        final TextView championName = findViewById(R.id.champion_name);
        final ImageView championImage = findViewById(R.id.champion_image);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                championName.setText(name);
                Picasso.get()
                        .load(img
                                .replace("http://localhost:", deviceIp))
                        .placeholder(R.mipmap.champion_placeholder)
                        .error(R.mipmap.champion_placeholder)
                        .into(championImage);
            }
        });
        getPositions();
    }

    private void getSummonerSpells() {
        final Call<ApiData> summonerSpells = client.getSummonerSpells();
        summonerSpells.enqueue(new Callback<ApiData>() {
            @Override
            public void onResponse(@NonNull Call<ApiData> call,
                                   @NonNull final Response<ApiData> response) {
                final ApiData spellsData = response.body();
                assert spellsData != null;
                if (response.isSuccessful() && spellsData.getSuccess()) {
                    mData = spellsData.getSummonerSpells();
                    spellButton1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showDialog(v, spellsData.getSummonerSpells());
                        }
                    });
                    spellButton2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showDialog(v, spellsData.getSummonerSpells());
                        }
                    });
                } else {
                    showError(spellsData.getError(), spellsData.getErrorCode());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiData> call, @NonNull Throwable throwable) {
                showException(call.request().url().host(), throwable);
            }
        });
    }

    private void getPositions() {
        final Call<ApiData> positions = client.getPositions();
        positions.enqueue(new Callback<ApiData>() {
            @Override
            public void onResponse(@NonNull Call<ApiData> call,
                                   @NonNull Response<ApiData> response) {
                ApiData positionsData = response.body();
                assert positionsData != null;
                if (response.isSuccessful() && positionsData.getSuccess()) {
                    mPositions = positionsData.getPositions();
                    initList();
                } else {
                    showError(positionsData.getError(), positionsData.getErrorCode());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiData> call, @NonNull Throwable throwable) {
                showException(call.request().url().host(), throwable);
            }
        });
    }

    private void getCurrentSpells() {
        final Call<ApiData> spells = client.getCurrentSpells();
        spells.enqueue(new Callback<ApiData>() {
            @Override
            public void onResponse(@NonNull Call<ApiData> call,
                                   @NonNull Response<ApiData> response) {
                assert response.body() != null;
                if (response.isSuccessful() && response.body().getSuccess()) {
                    Spell spellById1 =
                            getSpellById(Integer.valueOf(response.body().getSpells()[0]));
                    assert spellById1 != null;
                    spell1 = spellById1.getSpellId();
                    Spell spellById2 =
                            getSpellById(Integer.valueOf(response.body().getSpells()[1]));
                    assert spellById2 != null;
                    spell2 = spellById2.getSpellId();
                    updateButtons(spellButton1, spellById1.getPath());
                    updateButtons(spellButton2, spellById2.getPath());
                } else {
                    showError(response.body().getError(), response.body().getErrorCode());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiData> call,
                                  @NonNull Throwable throwable) {
                showException(call.request().url().host(), throwable);
            }
        });
    }

    private void initList() {
        ArrayAdapter adapter =
                new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, mPositions);
        Spinner positionsSpinner = findViewById(R.id.position_spinner);
        positionsSpinner.setAdapter(adapter);
        positionsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, final View v, final int position, long id) {
                final ApiInterface client = new ApiClient(getApplicationContext()).getClient();
                Call<ApiData> setPosition = client.setPosition(position);
                setPosition.enqueue(new Callback<ApiData>() {
                    @Override
                    public void onResponse(@NonNull Call<ApiData> call, @NonNull Response<ApiData> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(getApplicationContext(),
                                    getString(R.string.champion_select_changed) + mPositions[position],
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), getString(R.string.champion_select_error),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ApiData> call, @NonNull Throwable t) {
                        showException(call.request().url().host(), t);
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void showDialog(final View v, ArrayList<Spell> data) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_spells);

        SpellsAdapter adapter = new SpellsAdapter(context, data);
        RecyclerView recyclerView = dialog.findViewById(R.id.spells_list);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));

        adapter.setClickListener(new SpellsAdapter.ItemClickListener() {
            @Override
            public void onItemClick(final View view, final int position, final int id) {
                final ApiInterface client = new ApiClient(view.getContext()).getClient();
                RequestBody body;
                if (v.getId() == R.id.spell_button1) {
                    spell1 = position;
                    body = RequestBody.create(MediaType.parse("text/plain"), position + "," + spell2);
                } else {
                    spell2 = position;
                    body = RequestBody.create(MediaType.parse("text/plain"), spell1 + "," + position);
                }
                final Call<ApiData> setSpellsApi = client.setSpells(body);
                if (spell1 == spell2) {
                    Toast.makeText(getApplicationContext(), "Bad spells", Toast.LENGTH_SHORT).show();
                } else {
                    setSpellsApi.enqueue(new Callback<ApiData>() {
                        @Override
                        public void onResponse(@NonNull Call<ApiData> call,
                                               @NonNull Response<ApiData> response) {
                            assert response.body() != null;
                            if (response.isSuccessful() && response.body().getSuccess()) {
                                Toast.makeText(getApplicationContext(), "Changed spell",
                                        Toast.LENGTH_SHORT).show();
                                if (v.getId() == R.id.spell_button1) {
                                    updateButtons(spellButton1, mData.get(id).getPath());
                                } else {
                                    updateButtons(spellButton2, mData.get(id).getPath());
                                }

                                dialog.cancel();
                            } else {
                                showError("Failed to set spells", "Restart app");
                                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT)
                                        .show();
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<ApiData> call, @NonNull Throwable t) {
                            showException(call.request().url().host(), t);
                        }
                    });
                }
            }
        });
        recyclerView.setAdapter(adapter);
        dialog.show();
    }

    private void updateButtons(ImageButton button, String path) {
        Picasso.get()
                .load(PrefsHelper.getInstance(context).getString("device-ip") + path)
                .fit()
                .centerCrop()
                .into(button);
    }

    private Spell getSpellById(Integer id) {
        for (Spell spell : mData) {
            if (spell.getSpellId() == id) {
                return spell;
            }
        }
        return null;
    }

    @SuppressLint("SetTextI18n")
    private void showError(String message1, String message2) {
        errorLayout.setVisibility(View.VISIBLE);
        layout.setVisibility(View.GONE);
        code.setText(message1);
        error.setText(message2);
    }

    private void showException(final String string, final Throwable throwable) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String error1, error2;
                if (throwable instanceof SocketTimeoutException) {
                    error1 = "Connect timed out";
                    error2 = "Host: " + string;
                } else if (throwable instanceof UnknownHostException) {
                    error1 = "Unable to resolve host";
                    error2 = "Host: " + string;
                } else {
                    error1 = "Other exception";
                    error2 = "Contact developer";
                }
                showError(error1, error2);
            }
        });
    }

    class DataReceiver extends BroadcastReceiver  {

        @Override
        public void onReceive(Context context, Intent intent) {
            if(Objects.requireNonNull(intent.getAction()).equals("GET_CHAMPION_SELECT_DATA"))
            {
                String name = intent.getStringExtra("championName");
                String img = intent.getStringExtra("championImage");
                updateChampion(name, img);
            }
        }
    }
}
