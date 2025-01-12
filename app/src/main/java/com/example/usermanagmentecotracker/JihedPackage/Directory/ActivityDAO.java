package com.example.usermanagmentecotracker.JihedPackage.Directory;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;


import com.example.usermanagmentecotracker.JihedPackage.Entity.Activity;

import java.util.List;

@Dao
public interface ActivityDAO {
    @Insert
    Long insert(Activity activityData) ;

    @Query("SELECT * FROM activity WHERE userId = :userId ORDER BY date DESC ")
    List<Activity> getAll(int userId);

    @Update
    void updateActivity(Activity activity);
    @Delete()
    void delete(Activity activity);
    @Query("SELECT * FROM activity WHERE activityType = :activity")
    List<Activity> getActivitee(String activity);}