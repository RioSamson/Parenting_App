package com.example.cmpt276_2021_7_manganese.model;

import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * This class is for Task
 * There are some basic operations for controlling each task
 * @author Rio Samson
 */
public class Task {
    private ArrayList<TaskHistory> taskHistories = new ArrayList<>();
    String taskInfo;
    private final String TASK_CONTENT = "Task:";
    private Child curChildForTask;
    private ChildManager childManager;
    private int childIndex;

    public Task(String taskInfo) {
        childManager = ChildManager.getInstance();
        this.taskInfo = taskInfo;
        childIndex = 0;
    }

    public ArrayList<TaskHistory> getManager() {
        return taskHistories;
    }

    public void childDoneTask() {
        updateInformation();
        if (childIndex != -1) {
            addChildToHistory();
            if (childIndex == (childManager.getSize() - 1)) {
                childIndex = 0;
            } else {
                childIndex++;
            }
        }
    }

    private void addChildToHistory() {
        LocalDateTime timeNow = LocalDateTime.now();
        TaskHistory taskUpdate = new TaskHistory(childManager.getByIndex(childIndex),
                timeNow, childManager.getByIndex(childIndex).getPhotoUrl(),
                childManager.getByIndex(childIndex).getName(),
                childManager.getByIndex(childIndex).getId());
        taskHistories.add(taskUpdate);
    }

    public String getTaskInfo() {
        return taskInfo;
    }

    public void setTaskInfo(String taskInfo) {
        this.taskInfo = taskInfo;
    }

    public String getCurChildName() {
        updateInformation();
        if (childIndex == -1) {
            return "No Children";
        }
        curChildForTask = childManager.getByIndex(childIndex);
        return curChildForTask.getName();
    }

    public Child getCurChild() {
        return curChildForTask;
    }

    private void updateInformation() {
        childManager = ChildManager.getInstance();
        updateIndex();
    }

    //negative index for no child
    private void updateIndex() {
        int childrenNumber = childManager.getSize();
        if (childrenNumber == 0) {
            childIndex = -1;
        } else if (childIndex >= childrenNumber){
            childIndex = childrenNumber - 1;
        } else if (childIndex == -1) {
            childIndex = 0;
        }
    }

    public int historySize() {
        return taskHistories.size();
    }

    public int size() {
        return taskHistories.size();
    }

    public TaskHistory getHistoryInfo(int index) {
        return taskHistories.get(index);
    }

    @Override
    public String toString() {
        return TASK_CONTENT + taskInfo ;
    }
}