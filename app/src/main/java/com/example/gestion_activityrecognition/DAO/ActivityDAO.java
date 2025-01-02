package com.example.gestion_activityrecognition.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.gestion_activityrecognition.entity.Activity;

import java.util.List;

@Dao
public interface ActivityDAO {
    @Insert
    Long insert(Activity activityData) ;

    @Query("SELECT * FROM activity ORDER BY date DESC")
    List<Activity> getAll();

    @Update
    void updateActivity(Activity activity);
    @Delete()
    void delete(Activity activity);
    @Query("SELECT * FROM activity WHERE date = :date")
    List<Activity> getActiviteeByDate(String date);
}
