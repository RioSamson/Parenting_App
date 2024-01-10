package com.example.cmpt276_2021_7_manganese;


import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.cmpt276_2021_7_manganese.model.CoinResult;

@Database(entities = {CoinResult.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract CoinResultDao coinDao();

}



