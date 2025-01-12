package com.example.usermanagmentecotracker.JihedPackage.Entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "historique_table" ,
        foreignKeys = @ForeignKey(
        entity = User.class,
        parentColumns = "id",
        childColumns = "userId",
        onDelete = ForeignKey.CASCADE
))
public class HistoriqueTable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private float soundLevel;
    private String timestamp;
    private int userId;
    // Constructeur
    public HistoriqueTable(float soundLevel, String timestamp , int userId) {
        this.soundLevel = soundLevel;
        this.timestamp = timestamp;
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    // Getter et Setter pour `id`
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // Getter et Setter pour `soundLevel`
    public float getSoundLevel() {
        return soundLevel;
    }

    public void setSoundLevel(float soundLevel) {
        this.soundLevel = soundLevel;
    }

    // Getter et Setter pour `timestamp`
    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}