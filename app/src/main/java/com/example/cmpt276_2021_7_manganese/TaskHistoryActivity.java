//the background image is
//https://pin.it/2JZGj8E
package com.example.cmpt276_2021_7_manganese;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.cmpt276_2021_7_manganese.model.Child;
import com.example.cmpt276_2021_7_manganese.model.ChildManager;
import com.example.cmpt276_2021_7_manganese.model.Task;
import com.example.cmpt276_2021_7_manganese.model.TaskHistory;
import com.example.cmpt276_2021_7_manganese.model.TaskManager;

/**
 * This is the activity the displays the list of children that did each task
 * It shows the updated picture, name and date the history was done.
 * @author Rio Samson
 */
public class TaskHistoryActivity extends AppCompatActivity {
    TaskManager taskManager = TaskManager.getInstance();
    private static final String EXTRA_INTENT_MESSAGE = "Task Index";
    private int taskListIndex;
    private int defaultIndex = -1;
    private Task curTask;
    private ChildManager childManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); 
        setContentView(R.layout.activity_task_history);

        setupToolBar();

        childManager = ChildManager.getInstance();
        taskListIndex = getIntent().getIntExtra(EXTRA_INTENT_MESSAGE, defaultIndex);
        curTask = taskManager.getTask(taskListIndex);

        populateListView();
    }

    private void updateEverything() {
        for (int i = 0; i < curTask.historySize(); i++) {
            Child curChild = curTask.getHistoryInfo(i).getChild();
            TaskHistory curHistory = curTask.getHistoryInfo(i);
            Child ogChild = childManager.findById(curHistory.getId());
            if (ogChild != null) {
                if (!ogChild.getName().equals(curChild.getName())) {
                    curHistory.setName(ogChild.getName());
                    curHistory.setChild(ogChild);
                }
                if (!ogChild.getPhotoUrl().equals(curChild.getPhotoUrl())) {
                    curHistory.setUrl(ogChild.getPhotoUrl());
                    curHistory.setChild(ogChild);
                }
            } else {
                curHistory.setName("Deleted");
            }
        }
    }

    private void populateListView() {
        updateEverything();
        TaskHistoryAdapter adapter = new TaskHistoryAdapter(curTask.getManager(), this);
        ListView list = (ListView) findViewById(R.id.history_list_view);
        list.setAdapter(adapter);
    }

    public static Intent makeLaunchIntent(Context c, int taskIndex) {
        Intent intent = new Intent(c, TaskHistoryActivity.class);
        intent.putExtra(EXTRA_INTENT_MESSAGE, taskIndex);
        return intent;
    }

    private void setupToolBar() {
        Toolbar toolbar = findViewById(R.id.task_history_toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
         switch (item.getItemId()) {
             case android.R.id.home:
                 finish();
                 return true;
             default:
                 return super.onOptionsItemSelected(item);
         }
    }
}