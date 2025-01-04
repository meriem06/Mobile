package com.example.gestion_limunisite.DAO;

import androidx.room.Dao;
import androidx.room.Insert;

import com.example.gestion_limunisite.entity.User;
@Dao
public interface UserDAO {
    @Insert
    void insertUser(User user);
}
