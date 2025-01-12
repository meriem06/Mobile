package com.example.usermanagmentecotracker.JihedPackage.Entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.Date;

@Entity(tableName = "activity" ,
        foreignKeys = @ForeignKey(
                entity = User.class,
                parentColumns = "id",
                childColumns = "userId",
                onDelete = ForeignKey.CASCADE
        )
)
public class Activity {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String activityType;
    private int userId; // Foreign key to User table

    @TypeConverters(com.example.gestion_limunisite.entity.Converter.class)
    private Date date;


    public Activity(){

    }
    @Ignore
    public Activity(int id,String activityType, Date date , int userId) {
        this.id=id;
        this.activityType = activityType;
        this.userId = userId;

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