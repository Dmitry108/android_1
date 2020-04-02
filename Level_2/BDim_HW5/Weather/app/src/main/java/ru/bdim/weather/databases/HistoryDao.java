package ru.bdim.weather.databases;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.ArrayList;
import java.util.List;

import ru.bdim.weather.addiyional.Weather;

@Dao
public interface HistoryDao {
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    void insertStudent(HistoryEntity history);

    @Query("SELECT * FROM historyentity")
    List<HistoryEntity> getAll();

    @Update
    void updateHistory(HistoryEntity history);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertHistory(HistoryEntity history);

    @Query("DELETE FROM historyentity WHERE city = :city")
    void removeHistory(String city);

    @Query("SELECT * FROM historyentity ORDER BY city")
    List<HistoryEntity> sortByCity();

    @Query("SELECT * FROM historyentity ORDER BY date")
    List<HistoryEntity> sortByDate();

    @Query("SELECT * FROM historyentity ORDER BY temperature")
    List<HistoryEntity> sortByTemp();
}