package com.fadli.aplikasilaundry.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.fadli.aplikasilaundry.model.ModelLaundry;

import java.util.List;

@Dao
public interface LaundryDao {

    @Query("SELECT * FROM tbl_laundry")
    LiveData<List<ModelLaundry>> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertData(ModelLaundry... modelLaundries);

    @Query("DELETE FROM tbl_laundry WHERE uid= :uid")
    void deleteSingleData(int uid);

}