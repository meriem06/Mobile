package com.example.usermanagmentecotracker.JihedPackage.Directory;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;


import com.example.usermanagmentecotracker.JihedPackage.Entity.TemperatureEntry;

import java.util.List;

@Dao
public interface TemperatureDao {

    @Insert
    void insert(TemperatureEntry entry);

    @Query("SELECT * FROM temperature_entries")
    List<TemperatureEntry> getAllTemperatures();
    @Query("SELECT * FROM temperature_entries WHERE userId = :id")
    List<TemperatureEntry> getTemperaturesByuserid(int id);

}