package com.example.usermanagmentecotracker.JihedPackage.Entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Entity(
        tableName = "consommations",
        foreignKeys = @ForeignKey(
                entity = User.class,
                parentColumns = "id",
                childColumns = "userId",
                onDelete = ForeignKey.CASCADE
        )
)
public class Consommation implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private int userId; // Foreign key to User table
    private String distance;
    private String place;
    private String cost;
    private String time;

    private String date; // Field for the current date

    // Default constructor
    public Consommation() {
        this.date = getCurrentDate();
    }

    // Constructor with fields
    public Consommation(int userId, String distance, String place, String time, String cost) {
        this.userId = userId;
        this.distance = distance;
        this.place = place;
        this.time = time;
        this.cost = cost;
        this.date = getCurrentDate(); // Automatically set the current date
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public String getDistance() { return distance; }
    public void setDistance(String distance) { this.distance = distance; }
    public String getPlace() { return place; }
    public void setPlace(String place) { this.place = place; }
    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }
    public String getCost() { return cost; }
    public void setCost(String cost) { this.cost = cost; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    // Helper method to get the current date in "yyyy-MM-dd" format
    private String getCurrentDate() {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
    }
}
