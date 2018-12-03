package com.github.kko7.manaflux_android.Database;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.github.kko7.manaflux_android.R;

public class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView addressTxt, nameTxt;
    public ItemClickListener itemClickListener;

    public Holder(View itemView) {
        super(itemView);

        addressTxt = itemView.findViewById(R.id.addressTxt_row);
        nameTxt = itemView.findViewById(R.id.nameTxt_row);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        this.itemClickListener.onItemClick(v, getLayoutPosition());
    }


    public void setItemClickListener(ItemClickListener ic) {
        this.itemClickListener = ic;
    }
}