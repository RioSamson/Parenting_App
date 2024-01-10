package com.example.cmpt276_2021_7_manganese;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.cmpt276_2021_7_manganese.model.CoinResult;

import java.util.List;

@Dao
public interface CoinResultDao {

    @Query("SELECT * FROM coinresult")
    List<CoinResult> getAll();

    @Insert
    void insert(CoinResult coinResult);
}
