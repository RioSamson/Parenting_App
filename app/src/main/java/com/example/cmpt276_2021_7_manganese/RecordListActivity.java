package com.example.cmpt276_2021_7_manganese;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.cmpt276_2021_7_manganese.model.ChoiceAdapter;
import com.example.cmpt276_2021_7_manganese.model.CoinResult;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is for History or record of Flip coin activity
 * After each child flip coin, the child name, photo ,choice of coin, result of coin and
 * time will be record in this activity, when child don't choose head or tail, the photo will not be
 * shown in this screen, only show those children who choose head or tail
 *
 * @author Lingjie Li(Larry)
 */

public class RecordListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<CoinResult> coinResults = new ArrayList<>();
    private ChoiceAdapter adapter;
    private AppDatabase db;
    private Toolbar toolbar;

    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                adapter.notifyDataSetChanged();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        recyclerView = findViewById(R.id.recycle);
        toolbar = findViewById(R.id.tb_history);
        setUpToolBar(toolbar);
        adapter = new ChoiceAdapter(this, coinResults);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        db = Room.databaseBuilder(this, AppDatabase.class, "database-name").build();
        checkAll();

    }

    private void checkAll() {
        new Thread(() -> {
            List<CoinResult> datas = db.coinDao().getAll();
            coinResults.clear();
            coinResults.addAll(datas);
            handler.sendEmptyMessage(0);
        }).start();
    }

    public static Intent makeLaunchIntent(Context c) {
        return new Intent(c, RecordListActivity.class);
    }

    private void setUpToolBar(Toolbar toolbar) {
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.setDisplayHomeAsUpEnabled(true);
    }
}
