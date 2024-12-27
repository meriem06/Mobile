package com.example.usermanagmentecotracker.JihedPackage.Directory;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.usermanagmentecotracker.JihedPackage.Entity.Defis;

import java.util.List;

@Dao
public interface DefisDao {

    @Insert
    void insertDefis(Defis defis);

    @Update
    void updateDefis(Defis defis);

    @Delete
    void deleteDefis(Defis defis);

    @Query("SELECT * FROM defis WHERE userId = :userId")
    List<Defis> getDefisByUserId(int userId);

    @Query("SELECT * FROM defis")
    List<Defis> getAllDefis();

    @Query("DELETE FROM defis WHERE userId = :userId")
    void deleteDefisByUserId(int userId);
}
