package com.example.usermanagmentecotracker.Entity;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class UserWithDefis {
    @Embedded
    public User user;

    @Relation(parentColumn = "id", entityColumn = "userId")
    public List<Defis> defisList;
}
