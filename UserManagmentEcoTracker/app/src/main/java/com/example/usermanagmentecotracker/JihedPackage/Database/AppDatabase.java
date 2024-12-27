package com.example.usermanagmentecotracker.JihedPackage.Database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.usermanagmentecotracker.JihedPackage.Directory.DefisDao;
import com.example.usermanagmentecotracker.JihedPackage.Directory.UserDao;
import com.example.usermanagmentecotracker.JihedPackage.Directory.ConsommationDao;
import com.example.usermanagmentecotracker.JihedPackage.Entity.Defis;
import com.example.usermanagmentecotracker.JihedPackage.Entity.User;
import com.example.usermanagmentecotracker.JihedPackage.Entity.Consommation;

@Database(entities = {User.class, Defis.class, Consommation.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {

    public abstract UserDao userDao();
    public abstract DefisDao defisDao();
    public abstract ConsommationDao consommationDao();
}
