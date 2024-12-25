package com.example.usermanagmentecotracker.Directory;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.usermanagmentecotracker.Entity.Consommation;

import java.util.List;

@Dao
public interface ConsommationDao {
    @Insert
    void insertConsommation(Consommation consommation);

    @Transaction
    @Query("SELECT * FROM consommations WHERE userId = :userId")
    List<Consommation> getConsommationsForUser(int userId);

    @Delete
    void deleteConsommation(Consommation consommation);
}
