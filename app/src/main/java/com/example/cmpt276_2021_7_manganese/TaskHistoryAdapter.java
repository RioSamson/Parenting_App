package com.example.cmpt276_2021_7_manganese;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.cmpt276_2021_7_manganese.model.TaskHistory;

import java.time.LocalDateTime;
import java.util.List;

/**
 * This class helps with the list view of history. Helps organize the information
 * for each panel in a list view.
 * @author Rio Samson
 */
public class TaskHistoryAdapter extends BaseAdapter {
    private final Context context;
    private List<TaskHistory> taskHistories;

    public TaskHistoryAdapter(List<TaskHistory> taskHistories, Context context) {
        this.context=context;
        this.taskHistories = taskHistories;
    }

    @Override
    public int getCount() {
        return taskHistories.size();
    }

    @Override
    public Object getItem(int position) {
        return taskHistories.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = View.inflate(context, R.layout.list_view, null);
        TaskHistory history = taskHistories.get(position);
        TextView textView=view.findViewById(R.id.tv_name);
        ImageView img=view.findViewById(R.id.user_header);
        String name = history.getName();
            LocalDateTime date =  history.getDate();
            String fullInfo = name + "    (" + date.getMonthValue() + "/" +
                    date.getDayOfMonth() + "/" + date.getYear() + ")    ";
        textView.setText(fullInfo);

        Glide.with(this.context).load(history.getUrl()).placeholder(R.mipmap.default_head)
                .error(R.mipmap.default_head).into(img);
        return view;
    }
}
