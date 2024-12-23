package com.example.usermanagmentecotracker.Directory;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.usermanagmentecotracker.Entity.User;

@Dao
public interface UserDao {
    @Insert
    void insertUser(User user);

    @Query("SELECT * FROM users WHERE email = :email AND password = :password")
    User login(String email, String password);

    @Query("SELECT * FROM users WHERE email = :email")
    User getUserByEmail(String email);

    @Query("UPDATE users SET isVerified = 1 WHERE email = :email")
    void verifyUser(String email);

    @Query("UPDATE users SET name = :name WHERE email = :email")
    void updateUserName(String email, String name);
    @Query("UPDATE users SET email = :newEmail WHERE email = :currentEmail")
    void updateUserEmail(String newEmail, String currentEmail);

    @Query("UPDATE users SET birthdate = :birthdate WHERE email = :email")
    void updateUserBirthdate(String email, String birthdate);

    @Query("UPDATE users SET password = :password WHERE email = :email")
    void updateUserPassword(String email, String password);


    @Query("UPDATE users SET name = :name, birthdate = :birthdate, password = :password WHERE email = :email")
    void updateUserDetails(String email, String name, String birthdate, String password);

    @Query("UPDATE users SET isVerified = :isVerified WHERE email = :email")
    void updateUserVerificationStatus(String email, boolean isVerified);
}
