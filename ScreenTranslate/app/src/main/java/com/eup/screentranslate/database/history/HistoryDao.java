package com.eup.screentranslate.database.history;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.eup.screentranslate.model.History;

import java.util.List;

@Dao
public interface HistoryDao {
    @Query("SELECT * FROM history WHERE type = :type ORDER BY date DESC")
    LiveData<List<History>> getHistoryByType(int type);

    @Query("SELECT * FROM history WHERE text = :text")
    History checkExist(String text);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void update(History history);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(History history);

    @Query("DELETE FROM history")
    void deleteAll();

    @Delete
    void deleteSingle(History history);


}
