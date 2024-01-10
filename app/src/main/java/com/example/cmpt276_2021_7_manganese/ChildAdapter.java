package com.example.cmpt276_2021_7_manganese;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.cmpt276_2021_7_manganese.model.Child;

import java.util.List;

public class ChildAdapter extends BaseAdapter {
    private final Context context;
    private List<Child> childList;

    public ChildAdapter(List<Child> childList, Context context) {
        this.childList = childList;
        this.context=context;
    }

    @Override
    public int getCount() {
        return childList.size();
    }

    @Override
    public Object getItem(int position) {
        return childList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = View.inflate(context, R.layout.list_view, null);
        Child child = childList.get(position);
        TextView textView=view.findViewById(R.id.tv_name);
        ImageView img=view.findViewById(R.id.user_header);
        textView.setText(child.getName());

        Glide.with(this.context).load(child.getPhotoUrl()).placeholder(R.mipmap.default_head)
                .error(R.mipmap.default_head).into(img);
        view.findViewById(R.id.ll_item).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,AddChildActivity.class);
                intent.putExtra("child",child);
                intent.putExtra("Child",position);
                context.startActivity(intent);
            }
        });
        return view;
    }
}