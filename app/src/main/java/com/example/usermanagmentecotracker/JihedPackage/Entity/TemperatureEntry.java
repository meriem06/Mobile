package com.example.usermanagmentecotracker.JihedPackage.Entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "temperature_entries" ,
        foreignKeys = @ForeignKey(
                entity = User.class,
                parentColumns = "id",
                childColumns = "userId",
                onDelete = ForeignKey.CASCADE
        ))
public class TemperatureEntry {
    @PrimaryKey(autoGenerate = true)
    public int id;
    private int userId; // Foreign key to User table
    private String temperature;
    private String date;

    public TemperatureEntry(String temperature, String date , int userId) {
        this.temperature = temperature;
        this.date = date;
        this.userId = userId;
    }

    public String getTemperature() {
        return temperature;
    }

    public String getDate() {
        return date;
    }

    public String getRecommendation() {
        try {
            float temp = Float.parseFloat(temperature);
            if (temp > 32) {
                return "Climatisation recommandé";
            } else if (temp < 15) {
                return "Chauffage recommandé" ;
            } else {
                return "Température idéale";
            }

        } catch (NumberFormatException e) {
            return "Température invalide";
        }
    }
    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "TemperatureEntry{" +
                "temperature='" + temperature + '\'' +
                ", date='" + date + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}