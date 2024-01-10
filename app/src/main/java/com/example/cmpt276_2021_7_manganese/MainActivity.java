/**
 * This class is for main menu
 *
 * @author Shuai Li & Yam & Samson & Larry
 */

package com.example.cmpt276_2021_7_manganese;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.cmpt276_2021_7_manganese.model.ChildManager;
import com.example.cmpt276_2021_7_manganese.model.TaskManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
    private Button btnFlipCoin;
    private Button btnTimer;
    private Button btnChildManager;
    private Button btnWhoseTurn;
    private Button btwHelpScreen;
    private Button btwTakeBreath;

    private ChildManager childManager;
    private TaskManager taskManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);

        childManager = ChildManager.getInstance();
        taskManager = TaskManager.getInstance();

        setSupportActionBar(toolbar);
        setupFloatingActionButton();
        btnChildManager = findViewById(R.id.btn_childManager);
        btnFlipCoin = findViewById(R.id.btn_flipCoin);
        btnTimer = findViewById(R.id.btn_timer);
        btnWhoseTurn = findViewById(R.id.btn_whose_turn);
        btwHelpScreen = findViewById(R.id.btn_help);
        btwTakeBreath = findViewById(R.id.btn_take_breath);
        setListeners();

        load();
        load_task_info();
    }

    private void setupFloatingActionButton() {
        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(view -> {
            Intent addChild = AddChildActivity.makeLaunchIntent(MainActivity.this);
            startActivity(addChild);
        });
    }

    private void setListeners() {
        OnClick onClick = new OnClick();
        btnChildManager.setOnClickListener(onClick);
        btnFlipCoin.setOnClickListener(onClick);
        btnTimer.setOnClickListener(onClick);
        btnWhoseTurn.setOnClickListener(onClick);
        btwHelpScreen.setOnClickListener(onClick);
        btwTakeBreath.setOnClickListener(onClick);
    }

    private class OnClick implements View.OnClickListener {
        Intent intent = null;

        @Override
        public void onClick(View v) {
            switch (v.getId()) {

                case R.id.btn_childManager:
                    intent = new Intent(MainActivity.this, ChildrenListActivity.class);
                    break;

                case R.id.btn_flipCoin:
                    intent = FlipCoinActivity.makeLaunchIntent(MainActivity.this);
                    break;

                case R.id.btn_timer:
                    intent = TimeoutTimerActivity.makeLaunchIntent(MainActivity.this);
                    break;

                case R.id.btn_whose_turn:
                    intent = TasksActivity.makeLaunchIntent(MainActivity.this);
                    break;

                case R.id.btn_help:
                    intent = HelpActivity.makeLaunchIntent(MainActivity.this);
                    break;

                case R.id.btn_take_breath:
                    intent = TakeBreathActivity.makeLaunchIntent(MainActivity.this);
                    break;

            }
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private void load() {
        SharedPreferences prefs = this.getSharedPreferences("tag", MODE_PRIVATE);
        String jsonString = prefs.getString("save", "");
        if (!jsonString.equals("")) {
            childManager.load(jsonString);
        }
    }

    private void load_task_info() {
        SharedPreferences prefs = this.getSharedPreferences("tag_task", MODE_PRIVATE);
        String jsonStringTask = prefs.getString("save_task_info", "");
        if (!jsonStringTask.equals("")) {
            taskManager.loadTaskInfo(jsonStringTask);
        }
    }

}