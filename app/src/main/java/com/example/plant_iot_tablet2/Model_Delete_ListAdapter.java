package com.example.plant_iot_tablet2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

// ListAdapter 같은 클래스 내에 존재하도록.
public class Model_Delete_ListAdapter extends BaseAdapter {
    Context mContext = null;
    LayoutInflater mLayoutInflater = null;
    ArrayList<Model_Delete_ListItem> items;

    Toast toast;

    public Model_Delete_ListAdapter(Context context, ArrayList<Model_Delete_ListItem> data) {
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
        View view = mLayoutInflater.inflate(R.layout.list_item_model_delete, null);

        TextView name = (TextView) view.findViewById(R.id.name);
        name.setText(items.get(i).getName());

        ImageView radio = (ImageView) view.findViewById(R.id.radio);
        String check = items.get(i).getCheck();
        if (check.equals("O")) {
            radio.setImageResource(R.drawable.radio_check);
        } else {
            radio.setImageResource(R.drawable.radio);
        }

        return view;
    }
}