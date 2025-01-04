package com.example.gestion_limunisite.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
@Entity
public class User {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;
    private String birthdate;
    private String password;
    private String email;
    private boolean isVerified;

    public User() {}

    public User(String name, String birthdate, String password, String email) {
        this.name = name;
        this.birthdate = birthdate;
        this.password = password;
        this.email = email;
        this.isVerified = false;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getBirthdate() { return birthdate; }
    public void setBirthdate(String birthdate) { this.birthdate = birthdate; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public boolean isVerified() { return isVerified; }
    public void setVerified(boolean verified) { isVerified = verified; }
}
