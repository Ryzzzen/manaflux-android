package com.github.kko7.manaflux_android.Database;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.kko7.manaflux_android.R;
import com.github.kko7.manaflux_android.UserInterface.EditActivity;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Holder> {
    private final Context context;
    private final ArrayList<Device> devices;

    public Adapter(Context context, ArrayList<Device> devices) {
        this.context = context;
        this.devices = devices;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        @SuppressLint("InflateParams") View v =
                LayoutInflater.from(parent.getContext()).inflate(R.layout.list_model, null);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.addressTxt.setText(devices.get(position).getAddress());
        holder.nameTxt.setText(devices.get(position).getName());

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(int pos) {
                Intent i = new Intent(context, EditActivity.class);
                i.putExtra("ADDRESS", devices.get(pos).getAddress());
                i.putExtra("NAME", devices.get(pos).getName());
                i.putExtra("ID", devices.get(pos).getId());
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return devices.size();
    }
}