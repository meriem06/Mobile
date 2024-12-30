package com.example.usermanagmentecotracker.JihedPackage.Directory;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.usermanagmentecotracker.JihedPackage.Entity.Consommation;

import java.util.List;

@Dao
public interface ConsommationDao {
    @Insert
    void insertConsommation(Consommation consommation);

    @Transaction
    @Query("SELECT * FROM consommations WHERE userId = :userId")
    List<Consommation> getConsommationsForUser(int userId);

    @Transaction
    @Query("SELECT * FROM consommations WHERE userId = :userId ORDER BY CAST(distance AS REAL) ASC")
    List<Consommation> getConsommationsSortedByDistance(int userId);
    @Transaction
    @Query("SELECT * FROM consommations WHERE userId = :userId ORDER BY CAST(cost AS REAL) ASC")
    List<Consommation> getConsommationsSortedByCost(int userId);

    @Query("DELETE FROM consommations WHERE userId = :userId")
    void deleteAllConsommationsForUser(int userId);

    @Delete
    void deleteConsommation(Consommation consommation);
}