package com.example.plant_iot_tablet2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MasterActivity_Work_Device_ListAdapter extends BaseAdapter {
    Context mContext = null;
    LayoutInflater mLayoutInflater = null;
    ArrayList<MasterActivity_Work_Device_ListItem> items;

    public MasterActivity_Work_Device_ListAdapter(Context context, ArrayList<MasterActivity_Work_Device_ListItem> data) {
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
        View view = mLayoutInflater.inflate(R.layout.list_item_master_device, null);

        // 모델 정보.
        TextView model = (TextView) view.findViewById(R.id.model);
        TextView lastDate = (TextView) view.findViewById(R.id.lastDate);
        model.setText(items.get(i).getModel());
        lastDate.setText(items.get(i).getLastDate());

        // 사용 회원.
        TextView id = (TextView) view.findViewById(R.id.id);
        TextView name = (TextView) view.findViewById(R.id.name);
        TextView addDate = (TextView) view.findViewById(R.id.addDate);
        id.setText(items.get(i).getId());
        name.setText(items.get(i).getName());
        addDate.setText(items.get(i).getAddDate());

        return view;
    }
}
