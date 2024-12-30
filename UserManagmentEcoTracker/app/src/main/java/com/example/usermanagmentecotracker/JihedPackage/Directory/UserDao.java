package com.example.usermanagmentecotracker.JihedPackage.Directory;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.usermanagmentecotracker.JihedPackage.Entity.User;

@Dao
public interface UserDao {
    @Insert
    void insertUser(User user);

    @Query("SELECT * FROM users WHERE email = :email AND password = :password limit 1")
    User login(String email, String password);

    @Query("SELECT * FROM users WHERE email = :email")
    User getUserByEmail(String email);
    @Query("SELECT * FROM users WHERE id = :id")
    User getUserById(int id);

   // @Query("UPDATE users SET isVerified = 1 WHERE email = :email")
    //void verifyUser(String email);

    @Query("UPDATE users SET name = :name WHERE id = :id")
    void updateUserName(int id, String name);
    @Query("UPDATE users SET email = :newEmail WHERE id = :id")
    void updateUserEmail(String newEmail, int id);

    @Query("UPDATE users SET birthdate = :birthdate WHERE id = :id")
    void updateUserBirthdate(int id, String birthdate);

    @Query("UPDATE users SET password = :password WHERE id = :id")
    void updateUserPassword(int id, String password);

    @Query("UPDATE users SET profileImagePath = :imagePath WHERE id = :id")
    void updateImagePath(int id, String imagePath);

    @Query("UPDATE users SET name = :name, birthdate = :birthdate, password = :password WHERE email = :email")
    void updateUserDetails(String email, String name, String birthdate, String password);

    @Query("UPDATE users SET isVerified = :isVerified WHERE email = :email")
    void updateUserVerificationStatus(String email, boolean isVerified);

}
