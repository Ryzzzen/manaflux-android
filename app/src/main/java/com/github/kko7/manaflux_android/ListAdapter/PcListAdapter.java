package com.github.kko7.manaflux_android.ListAdapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.github.kko7.manaflux_android.R;

import java.util.ArrayList;
import java.util.Objects;

public class PcListAdapter extends ArrayAdapter<PC> {

    private Context mContext;
    private int mResource;

    private static class ViewHolder {
        TextView name;
        TextView ipAddress;
    }

    /**
     * @param context context
     * @param resource resources
     * @param objects objects
     */
    public PcListAdapter(Context context, int resource, ArrayList<PC> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        String ipAddress = Objects.requireNonNull(getItem(position)).getAddress();
        String pcName = Objects.requireNonNull(getItem(position)).getName();

        PC pc = new PC(ipAddress, pcName);

        ViewHolder holder;

        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);
            holder= new ViewHolder();
            holder.ipAddress = convertView.findViewById(R.id.ip_text);
            holder.name = convertView.findViewById(R.id.name_text);

            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
        }

        holder.ipAddress.setText(pc.getAddress());
        holder.name.setText(pc.getName());


        return convertView;
    }
}

























