package com.example.usermanagmentecotracker.JihedPackage.Entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "consommations",
        foreignKeys = @ForeignKey(
                entity = User.class,
                parentColumns = "id",
                childColumns = "userId",
                onDelete = ForeignKey.CASCADE
        )
)
public class Consommation {
    @PrimaryKey(autoGenerate = true)
    private int id;

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    private int userId; // Foreign key to User table
    private String distance;
    private String place;
    private String cost;
    private String time;

    public Consommation() {}

    public Consommation(int userId, String distance, String place, String time) {
        this.userId = userId;
        this.distance = distance;
        this.place = place;
        this.time = time;
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
}
