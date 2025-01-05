package com.example.usermanagmentecotracker.JihedPackage.Database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.usermanagmentecotracker.JihedPackage.Directory.ActivityDAO;
import com.example.usermanagmentecotracker.JihedPackage.Directory.DefisDao;
import com.example.usermanagmentecotracker.JihedPackage.Directory.HistoriqueDao;
import com.example.usermanagmentecotracker.JihedPackage.Directory.LuminositeDAO;

import com.example.usermanagmentecotracker.JihedPackage.Directory.UserDao;
import com.example.usermanagmentecotracker.JihedPackage.Directory.ConsommationDao;
import com.example.usermanagmentecotracker.JihedPackage.Entity.Activity;
import com.example.usermanagmentecotracker.JihedPackage.Entity.Defis;
import com.example.usermanagmentecotracker.JihedPackage.Entity.HistoriqueTable;
import com.example.usermanagmentecotracker.JihedPackage.Entity.Luminosite;
import com.example.usermanagmentecotracker.JihedPackage.Entity.User;
import com.example.usermanagmentecotracker.JihedPackage.Entity.Consommation;

@Database(entities = {User.class, Defis.class, Consommation.class , HistoriqueTable.class , Luminosite.class , Activity.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {

    public abstract UserDao userDao();
    public abstract DefisDao defisDao();
    public abstract ConsommationDao consommationDao();
    public abstract HistoriqueDao historiqueDao();
    public abstract LuminositeDAO luminositeDao();
    public abstract ActivityDAO activityDAO();
}
