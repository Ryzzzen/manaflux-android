package com.github.kko7.manaflux_android.UserInterface.Dashboard;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.kko7.manaflux_android.Helpers.PrefsHelper;
import com.github.kko7.manaflux_android.R;
import com.squareup.picasso.Picasso;

public class ProfileFragment extends Fragment {

    public ProfileFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, Bundle savedInstanceState) {
        final PrefsHelper prefsHelper = PrefsHelper.getInstance(view.getContext());
        final String summonerDisplayName = prefsHelper.getString("summoner-name");
        final String summonerIcon = prefsHelper.getString("summoner-icon");
        final String deviceIp = prefsHelper.getString("device-ip");
        final ImageView summonerImage = view.findViewById(R.id.summonerImage);
        final TextView summonerName = view.findViewById(R.id.summonerName);
        Picasso.get()
                .load(summonerIcon
                        .replace("http://localhost:", deviceIp))
                .placeholder(R.mipmap.champion_placeholder)
                .error(R.mipmap.champion_placeholder)
                .into(summonerImage);
        summonerName.setText(summonerDisplayName);
    }
}