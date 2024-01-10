package com.example.cmpt276_2021_7_manganese.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class CoinResult {

    @PrimaryKey
    @NonNull
    public String uid = "123455";
    public String time;
    @ColumnInfo(name = "currentchose")
    public String currentChose;
    public String result;
    public String user;
    public String photo;

    public CoinResult(String uid, String time, String currentChose, String result, String user, String photo) {
        this.uid = uid;
        this.time = time;
        this.currentChose = currentChose;
        this.result = result;
        this.user = user;
        this.photo = photo;
    }
}
