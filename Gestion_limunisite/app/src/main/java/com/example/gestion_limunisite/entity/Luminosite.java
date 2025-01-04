package com.example.gestion_limunisite.entity;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.Relation;
import androidx.room.TypeConverters;

import java.util.Date;
@Entity(foreignKeys = @ForeignKey(
        entity = User.class,
        parentColumns = "id", // Colonne dans l'entité `User`
        childColumns = "userId", // Colonne dans l'entité `Luminosite`
        onDelete = ForeignKey.CASCADE
))
public class Luminosite {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private float intensite;
    private boolean isNormal;
    @TypeConverters(Converter.class)
    private Date date;

    // Ajout de la colonne userId
    private int userId;
    @Ignore
     User user ;

    public Luminosite(float intensite, boolean isNormal, Date date, User user) {

        this.intensite = intensite;
        this.isNormal = isNormal;
        this.date = date;
        this.user = user;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    // Constructeurs, getters et setters
    public Luminosite() {
    }
    @Ignore
    public Luminosite(float intensite, boolean isNormal, Date date, int userId) {
        this.intensite = intensite;
        this.isNormal = isNormal;
        this.date = date;
        this.userId = userId;
    }

    @Ignore
    public Luminosite(int id, float intensite, boolean isNormal, Date date, int userId) {
        this.id = id;
        this.intensite = intensite;
        this.isNormal = isNormal;
        this.date = date;
        this.userId = userId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public float getIntensite() {
        return intensite;
    }

    public void setIntensite(float intensite) {
        this.intensite = intensite;
    }

    public boolean isNormal() {
        return isNormal;
    }

    public void setNormal(boolean normal) {
        isNormal = normal;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
