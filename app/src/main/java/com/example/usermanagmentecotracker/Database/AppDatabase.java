package com.example.usermanagmentecotracker.Database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.usermanagmentecotracker.Directory.DefisDao;
import com.example.usermanagmentecotracker.Directory.UserDao;
import com.example.usermanagmentecotracker.Directory.ConsommationDao;
import com.example.usermanagmentecotracker.Entity.Defis;
import com.example.usermanagmentecotracker.Entity.User;
import com.example.usermanagmentecotracker.Entity.Consommation;

@Database(entities = {User.class, Defis.class, Consommation.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {

    public abstract UserDao userDao();
    public abstract DefisDao defisDao();
    public abstract ConsommationDao consommationDao();
}
