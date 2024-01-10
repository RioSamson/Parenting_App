package com.example.cmpt276_2021_7_manganese.model;

import java.time.LocalDateTime;

/**
 * This is a class that encompasses all the information that is needed
 * to show the user information about task history.
 * Records the child's name, picture and date the task was done on.
 * @author Rio Samson
 */
public class TaskHistory {
    private Child child;
    private LocalDateTime date;
    private String url;
    private String name;
    private int id;

    public TaskHistory(Child child, LocalDateTime date, String url, String name, int id) {
        this.child = child;
        this.date = date;
        this.url = url;
        this.name = name;
        this.id = id;
    }

    public Child getChild() {
        return child;
    }

    public int getId() {
        return id;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public String getUrl() {
        return url;
    }

    public String getName() {
        return name;
    }

    public void setChild(Child child) {
        this.child = child;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setName(String name) {
        this.name = name;
    }
}
