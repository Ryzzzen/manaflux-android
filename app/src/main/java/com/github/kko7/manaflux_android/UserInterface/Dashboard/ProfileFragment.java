package com.github.kko7.manaflux_android.UserInterface.Dashboard;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.kko7.manaflux_android.Connection.ApiClient;
import com.github.kko7.manaflux_android.Connection.ApiInterface;
import com.github.kko7.manaflux_android.Helpers.PrefsHelper;
import com.github.kko7.manaflux_android.Models.ApiData;
import com.github.kko7.manaflux_android.R;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {

    private ApiInterface client;
    private String summonerIcon;
    private String summonerDisplayName;

    public ProfileFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, Bundle savedInstanceState) {
        final String deviceIp = PrefsHelper.getInstance(view.getContext()).getString("device-ip");
        final ImageView summonerImage = view.findViewById(R.id.summonerImage);
        final TextView summonerName = view.findViewById(R.id.summonerName);
        client = new ApiClient(view.getContext()).getClient();
        getSummonerData();
        Picasso.get()
                .load(summonerIcon
                        .replace("http://localhost:", deviceIp))
                .placeholder(R.mipmap.champion_placeholder)
                .error(R.mipmap.champion_placeholder)
                .into(summonerImage);
        summonerName.setText(summonerDisplayName);
    }

    private void getSummonerData() {
        final Call<ApiData> summonerData = client.getSummoner();
        summonerData.enqueue(new Callback<ApiData>() {
            @Override
            public void onResponse(@NonNull Call<ApiData> call,
                                   @NonNull final Response<ApiData> response) {
                assert response.body() != null;
                if (response.isSuccessful() && response.body().getSuccess()) {
                    summonerDisplayName = response.body().getSummonerName();
                    summonerIcon = response.body().getSummonerIcon();
                } else {
                    summonerDisplayName = "Failed to get data";
                    summonerIcon = null;
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiData> call, @NonNull Throwable throwable) {
                summonerDisplayName = "Failed to get data";
                summonerIcon = null;
            }
        });
    }
}