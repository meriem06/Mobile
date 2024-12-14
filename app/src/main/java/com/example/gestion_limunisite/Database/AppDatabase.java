package com.example.gestion_limunisite.Database;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import android.content.Context;

import com.example.gestion_limunisite.DAO.LuminositeDAO;
import com.example.gestion_limunisite.entity.Converter;
import com.example.gestion_limunisite.entity.Luminosite;

@Database(entities = {Luminosite.class}, version = 2, exportSchema = true)
@TypeConverters({Converter.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract LuminositeDAO luminositeDAO();

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "luminosite_database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
