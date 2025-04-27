package com.fadli.aplikasilaundry.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.fadli.aplikasilaundry.database.dao.LaundryDao;
import com.fadli.aplikasilaundry.model.ModelLaundry;

@Database(entities = {ModelLaundry.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract LaundryDao laundryDao();
}