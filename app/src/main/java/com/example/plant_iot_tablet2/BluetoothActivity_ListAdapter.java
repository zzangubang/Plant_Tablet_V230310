package com.example.plant_iot_tablet2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class BluetoothActivity_ListAdapter extends BaseAdapter {
    Context mContext = null;
    LayoutInflater mLayoutInflater = null;
    ArrayList<BluetoothActivity_ListItem> items;

    public BluetoothActivity_ListAdapter(Context context, ArrayList<BluetoothActivity_ListItem> data) {
        mContext = context;
        items = data;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
       return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        View view = mLayoutInflater.inflate(R.layout.list_item_bluetooth, null);

        TextView name = (TextView) view.findViewById(R.id.name);
        TextView address = (TextView) view.findViewById(R.id.address);
        name.setText(items.get(i).getName());
        address.setText(items.get(i).getAddress());

        return view;
    }
}
