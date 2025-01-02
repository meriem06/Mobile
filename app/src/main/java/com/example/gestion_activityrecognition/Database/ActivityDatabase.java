package com.example.gestion_activityrecognition.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.gestion_activityrecognition.DAO.ActivityDAO;
import com.example.gestion_activityrecognition.entity.Activity;
import com.example.gestion_activityrecognition.entity.Converter;

@Database(entities = {Activity.class}, version = 2, exportSchema = true)
@TypeConverters({Converter.class})
public abstract class ActivityDatabase extends RoomDatabase {
    public abstract ActivityDAO activityDAO();

    private static volatile ActivityDatabase INSTANCE;

    public static ActivityDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (ActivityDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    ActivityDatabase.class, "activity_database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}

