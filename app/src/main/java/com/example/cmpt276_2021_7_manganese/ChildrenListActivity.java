package com.example.cmpt276_2021_7_manganese;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.cmpt276_2021_7_manganese.model.ChildManager;
import com.example.cmpt276_2021_7_manganese.databinding.ActivityManagingMyChildrenDataBinding;
/**
 * This class is for Children manager activity
 * after add children data, user can manage child data at this activity
 *
 * @author Shuai Li & Yam
 */
public class ChildrenListActivity extends AppCompatActivity {
    private static int index = 0; // get child by index in manager
    private ChildManager manager;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private TextView tv_notice;
    private TextView emptyListInfo;
    private ListView lv_child_data;
    private final int REQUEST_CODE_AddCHILD = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_managing_my_children_data);
        manager = ChildManager.getInstance();
        lv_child_data = findViewById(R.id.lv_manage_child);
        Toolbar toolbar = findViewById(R.id.tb_manage_child);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        tv_notice = findViewById(R.id.tv_manage_child_info);
        tv_notice.setSelected(true);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();
        emptyInfo();
    }

    private void registerClick() {
        lv_child_data.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                Intent intent = AddChildActivity.makeLaunchIntentWithPosition(ChildrenListActivity.this, position);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_AddCHILD) {
            emptyInfo();
        }
    }

    private void populateListView() {
        ChildAdapter adapter = new ChildAdapter(manager.getManager(), this);
        lv_child_data.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void emptyInfo() {
        emptyListInfo = findViewById(R.id.tv_emptyList);
        if (manager.getSize() <= 0) {
            emptyListInfo.setVisibility(View.VISIBLE);
        } else {
            emptyListInfo.setVisibility(View.GONE);
        }
    }

    public static Intent makeLaunchIntent(Context c) {
        return new Intent(c, ChildrenListActivity.class);
    }

    public static Intent makeLaunchIntent(Context c, String message, int position) {
        index = position;
        Intent intent = new Intent(c, AddChildActivity.class);
        intent.putExtra("Child", message);
        return intent;
    }

    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        populateListView();
        emptyInfo();
    }
}