package com.example.gestion_activityrecognition.entity;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.Date;

@Entity(tableName = "activity")
public class Activity {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String activityType;

    @TypeConverters(Converter.class)
    private Date date;
    int userId;


    public Activity(){

    }
    @Ignore
 public Activity(int id,String activityType, Date date) {
        this.id=id;
        this.activityType = activityType;

        this.date = date;

    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }



    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}