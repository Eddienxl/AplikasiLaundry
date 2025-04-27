package com.fadli.aplikasilaundry.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.fadli.aplikasilaundry.database.DatabaseClient;
import com.fadli.aplikasilaundry.database.dao.LaundryDao;
import com.fadli.aplikasilaundry.model.ModelLaundry;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AddDataViewModel extends AndroidViewModel {

    LaundryDao pengeluaranDao;

    public AddDataViewModel(@NonNull Application application) {
        super(application);

        pengeluaranDao = DatabaseClient.getInstance(application).getAppDatabase().laundryDao();
    }

    public void addDataLaundry(final String nama_jasa, final int items, final String alamat, final int price) {
        Completable.fromAction(() -> {
                    ModelLaundry pengeluaran = new ModelLaundry();
                    pengeluaran.nama_jasa = nama_jasa;
                    pengeluaran.items = items;
                    pengeluaran.alamat = alamat;
                    pengeluaran.harga = price;
                    pengeluaranDao.insertData(pengeluaran);
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

}