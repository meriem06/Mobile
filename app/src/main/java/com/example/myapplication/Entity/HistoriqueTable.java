package com.example.myapplication.Entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "historique_table")
public class HistoriqueTable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private float soundLevel;
    private String timestamp;

    // Constructeur
    public HistoriqueTable(float soundLevel, String timestamp) {
        this.soundLevel = soundLevel;
        this.timestamp = timestamp;
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
