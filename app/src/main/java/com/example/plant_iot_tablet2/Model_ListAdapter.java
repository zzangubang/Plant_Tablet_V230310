package com.example.plant_iot_tablet2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Model_ListAdapter extends BaseAdapter {
    Context mContext = null;
    LayoutInflater mLayoutInflater = null;
    ArrayList<Model_ListItem> items;

    Toast toast;

    public Model_ListAdapter(Context context, ArrayList<Model_ListItem> data) {
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
        View view = mLayoutInflater.inflate(R.layout.list_item_model, null);

        TextView name = (TextView) view.findViewById(R.id.name);
        name.setText(items.get(i).getName());

        ImageView button_edit = (ImageView) view.findViewById(R.id.button_edit);
        button_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, Activity_ListRevise.class);
                intent.putExtra("model", items.get(i).getModel());
                intent.putExtra("name", items.get(i).getName());
                intent.putExtra("id", items.get(i).getId());
                ((Activity) mContext).startActivity(intent);
            }
        });

        return view;
    }
}
