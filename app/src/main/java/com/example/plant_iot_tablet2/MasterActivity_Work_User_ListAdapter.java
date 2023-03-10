package com.example.plant_iot_tablet2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MasterActivity_Work_User_ListAdapter extends BaseExpandableListAdapter {
    ArrayList<MasterActivity_Work_User_Parent_ListItem> parentItems;
    ArrayList<ArrayList<MasterActivity_Work_User_Child_ListItem>> childItems;

    @Override
    public int getGroupCount() {
        return parentItems.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return childItems.get(i).size();
    }

    @Override
    public MasterActivity_Work_User_Parent_ListItem getGroup(int i) {
        return parentItems.get(i);
    }

    @Override
    public MasterActivity_Work_User_Child_ListItem getChild(int i, int i1) {
        return childItems.get(i).get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.list_item_master_user_parent, viewGroup, false);

        TextView nameT = (TextView) v.findViewById(R.id.name);
        TextView phoneT = (TextView) v.findViewById(R.id.phone);
        TextView dateT = (TextView) v.findViewById(R.id.signDate);
        ImageView button_detail = (ImageView) v.findViewById(R.id.button_detail);

        // 목록 펼쳐짐 여부.
        if(b) {
            button_detail.setImageResource(R.drawable.list_detail_up_icon);
        }
        else {
            button_detail.setImageResource(R.drawable.list_detail_icon);
        }

        nameT.setText(getGroup(i).getName());
        phoneT.setText(getGroup(i).getPhone());
        dateT.setText(getGroup(i).getDate());

        return v;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.list_item_master_user_child, viewGroup, false);

        TextView modelT = (TextView) v.findViewById(R.id.model);
        TextView dateT = (TextView) v.findViewById(R.id.addDate);

        modelT.setText(getChild(i, i1).getModel());
        dateT.setText(getChild(i, i1).getDate());

        return v;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    public void addItem(int groupPosition, MasterActivity_Work_User_Child_ListItem item) {
        childItems.get(groupPosition).add(item);
    }
}
