package com.example.cmpt276_2021_7_manganese;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cmpt276_2021_7_manganese.model.Child;
import com.example.cmpt276_2021_7_manganese.model.ChildManager;
import com.example.cmpt276_2021_7_manganese.model.Task;
import com.example.cmpt276_2021_7_manganese.model.TaskManager;

/**
 * This class is for adding Task
 * after add, the task will show on the TasksActivity
 *
 * @author Shuai Li
 */
public class AddTasksActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "Task";
    private TaskManager taskManager;
    private EditText inputTask;
    private boolean checkValidInput = false;
    private String taskInfo;
    private final String SAVED = "Saved";
    private final String INVALID_INPUT = "Cannot save empty information";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tasks);

        Toolbar toolbar = findViewById(R.id.add_task_toolbar);
        inputTask = findViewById(R.id.et_add_task);
        inputTask.addTextChangedListener(tw);
        taskManager = TaskManager.getInstance();

        setUpToolBar(toolbar);
    }

    private TextWatcher tw = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String strName = inputTask.getText().toString().trim();
            if (strName.length() <= 0) {
                checkValidInput = false;
                return;
            }
            checkValidInput = true;
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    private void setUpToolBar(Toolbar toolbar) {
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_backup:
                if (checkValidInput) {
                    taskInfo = inputTask.getText().toString();
                    taskManager.add(new Task(taskInfo));
                    finish();
                } else {
                    Toast.makeText(this, INVALID_INPUT, Toast.LENGTH_SHORT).show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_action_bar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public static Intent makeLaunchIntent(Context c) {
        Intent intent = new Intent(c, AddTasksActivity.class);
        return intent;
    }

    private void jsonSave() {
        String jsonStringForTask = taskManager.getGsonStringForTask();
        SharedPreferences prefs = this.getSharedPreferences("tag_task", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("save_task_info", jsonStringForTask);
        editor.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        super.onDestroy();
        jsonSave();
        this.finish();
    }

}