package com.github.kko7.manaflux_android.Models;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.github.kko7.manaflux_android.Helpers.PrefsHelper;
import com.github.kko7.manaflux_android.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SpellsAdapter extends RecyclerView.Adapter<SpellsAdapter.ViewHolder> {

    private ArrayList<Spell> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    public SpellsAdapter(Context context, ArrayList<Spell> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recycler_spell, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.spellName.setText(mData.get(position).getSpellName());
        holder.spellButton.setContentDescription(String.valueOf(mData.get(position).getSpellId()));
        Picasso.get()
                .load("http://"
                        + PrefsHelper.getInstance(holder.itemView.getContext()).getString("device-ip")
                        + mData.get(position).getPath())
                .fit()
                .centerCrop()
                .into(holder.spellButton);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position, int id);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageButton spellButton;
        TextView spellName;

        ViewHolder(View itemView) {
            super(itemView);
            spellButton = itemView.findViewById(R.id.spell_image);
            spellName = itemView.findViewById(R.id.spell_name);
            spellButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mClickListener.onItemClick(v,
                            Integer.parseInt(String.valueOf(spellButton.getContentDescription())),
                            getAdapterPosition());
                }
            });
        }
    }
}