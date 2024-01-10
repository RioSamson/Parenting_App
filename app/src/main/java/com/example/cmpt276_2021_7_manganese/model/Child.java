package com.example.cmpt276_2021_7_manganese.model;

import java.io.Serializable;

/**
 * This class is for Child data
 * Child's name and photo
 * @author  Shuai Li
 */

public class Child  implements Serializable {
    String name;
    String PhotoUrl;
    private int id;
    public Child(String name, String PhotoUrl, int id) {
        this.name = name;
        this.PhotoUrl = PhotoUrl;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setPhotoUrl(String photoUrl) {
        PhotoUrl = photoUrl;
    }

    public Child(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getPhotoUrl() { return PhotoUrl; }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Name: " + name ;
    }
}
