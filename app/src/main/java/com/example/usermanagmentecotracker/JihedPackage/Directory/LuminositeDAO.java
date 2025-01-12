package com.example.usermanagmentecotracker.JihedPackage.Directory;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;


import com.example.usermanagmentecotracker.JihedPackage.Entity.Luminosite;

import java.util.List;

@Dao
public interface LuminositeDAO {
    @Insert
    long insert(Luminosite luminosite);

    @Query("SELECT * FROM luminosite")
    List<Luminosite> getAll();

   @Delete
   void deleteLuminosite(Luminosite luminosite);

    @Update
    void updateLuminosite(Luminosite luminosite);

    @Query("SELECT * FROM luminosite WHERE id = :id")
    Luminosite getLuminositeById(long id);

}