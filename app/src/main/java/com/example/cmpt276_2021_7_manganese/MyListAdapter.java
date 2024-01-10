package com.example.cmpt276_2021_7_manganese;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.cmpt276_2021_7_manganese.model.Child;

import java.util.ArrayList;
import java.util.List;

public class MyListAdapter extends BaseAdapter {

    private ArrayList<Child> listItems;
    private LayoutInflater layoutInflater;

    public MyListAdapter(Context context, ArrayList<Child> childrenList) {
        layoutInflater = LayoutInflater.from(context);
        this.listItems=childrenList;
    }

    public List<Child> getListItems() {
        return listItems;
    }

    public void setListItems(ArrayList<Child> listItems) {
        this.listItems = listItems;
    }

    @Override
    public int getCount() {

        return listItems.size();
    }

    @Override
    public Child getItem(int position) {

        return listItems.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


            convertView = layoutInflater
                    .inflate(R.layout.simple_list_item_data, null);

        TextView textView = convertView.findViewById(R.id.tv_content);
        textView.setText(listItems.get(position).getName());
        return convertView;
    }


}
