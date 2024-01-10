package com.example.cmpt276_2021_7_manganese.model;

import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.Iterator;
/**
 * This class is for TaskManager
 * There are some basic operations for controlling taskManager
 * It uses singleton model
 * @author Rio Samson + Shuai Li
 */
public class TaskManager implements Iterable<Task> {
    private ArrayList<Task> tasksList = new ArrayList<>();
    private static TaskManager TaskInstance;
    private final String INDEX_OUT_OF_RANGE = "Index out of range";

    public TaskManager() {
    }

    public static TaskManager getInstance() {
        if (TaskInstance == null) {
            TaskInstance = new TaskManager();
        }
        return TaskInstance;
    }

    public void add(Task task) {
        tasksList.add(task);
    }

    public void removeTask(int index) {
        if (index < 0 || index > tasksList.size()) {
            throw new IndexOutOfBoundsException(INDEX_OUT_OF_RANGE);
        } else {
            tasksList.remove(index);
        }
    }

    public int getSize() {
        return tasksList.size();
    }

    public Task getTask(int index) {
        return tasksList.get(index);
    }

    public String[] StringTaskData() {
        String[] Str = new String[tasksList.size()];
        for (int i = 0; i < tasksList.size(); i++) {
            Str[i] = tasksList.get(i).getTaskInfo();
        }
        return Str;
    }

    @Override
    public Iterator<Task> iterator() {
        return tasksList.iterator();
    }

    public String getGsonStringForTask() {
        Gson gsonTask = new Gson();
        String jsonStringForTask = gsonTask.toJson(this);
        return jsonStringForTask;
    }

    public void loadTaskInfo(String jsonString) {
        Gson gsonTask = new Gson();
        TaskManager loaded = gsonTask.fromJson(jsonString, TaskManager.class);
        tasksList = loaded.tasksList;
    }
}
