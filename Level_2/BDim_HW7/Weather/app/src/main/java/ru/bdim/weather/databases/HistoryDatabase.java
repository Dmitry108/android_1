package ru.bdim.weather.databases;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = HistoryEntity.class, version = 1, exportSchema = false)
public abstract class HistoryDatabase extends RoomDatabase {
    public abstract HistoryDao getHistoryDao();
}
