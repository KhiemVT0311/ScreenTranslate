package com.eup.screentranslate.database.history;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.eup.screentranslate.model.History;

@Database(entities = {History.class}, exportSchema = false, version = 1)
public abstract class HistoryDatabase extends RoomDatabase {
    private static final String DB_NAME = "history_db.db";
    private static HistoryDatabase instance;

    //synchronized ddoongf booj hoas duwx liejeu trong cac thread
    public static synchronized HistoryDatabase getInstance(Context context) {
        if (instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),HistoryDatabase.class,DB_NAME)
                    .fallbackToDestructiveMigration()
                    .build();
        }

        return instance;
    }

    public abstract HistoryDao historyDao();
}
