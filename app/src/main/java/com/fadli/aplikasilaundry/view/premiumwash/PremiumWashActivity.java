package com.fadli.aplikasilaundry.view.premiumwash;

import android.app.Activity;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.fadli.aplikasilaundry.R;
import com.fadli.aplikasilaundry.utils.FunctionHelper;
import com.fadli.aplikasilaundry.viewmodel.AddDataViewModel;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import im.delight.android.location.SimpleLocation;

public class PremiumWashActivity extends AppCompatActivity {

    public static final String DATA_TITLE = "TITLE";
    private int hargaKaos = 9000, hargaCelana = 8000, hargaJaket = 10000, hargaSprei = 70000, hargaKarpet = 250000;
    private int itemCount1 = 0, itemCount2 = 0, itemCount3 = 0, itemCount4 = 0, itemCount5 = 0;
    private int countKaos, countCelana, countJaket, countSprei, countKarpet, totalItems, totalPrice;
    private String strTitle, strCurrentLocation, strCurrentLatLong;
    private double strCurrentLatitude;
    private double strCurrentLongitude;
    private SimpleLocation simpleLocation;
    private AddDataViewModel addDataViewModel;
    private Button btnCheckout;
    private ImageView imageAdd1, imageAdd2, imageAdd3, imageAdd4, imageAdd5,
            imageMinus1, imageMinus2, imageMinus3, imageMinus4, imageMinus5;
    private TextView tvTitle, tvInfo, tvJumlahBarang, tvTotalPrice, tvKaos, tvCelana, tvJaket, tvSprei, tvKarpet,
            tvPriceKaos, tvPriceCelana, tvPriceJaket, tvPriceSprei, tvPriceKarpet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laundry);

        setLocation();
        setStatusbar();
        setInitLayout();
        setItemData();
        getCurrentLocation();
    }

    private void setLocation() {
        simpleLocation = new SimpleLocation(this);

        if (!simpleLocation.hasLocationEnabled()) {
            SimpleLocation.openSettings(this);
        }

        strCurrentLatitude = simpleLocation.getLatitude();
        strCurrentLongitude = simpleLocation.getLongitude();
        strCurrentLatLong = strCurrentLatitude + "," + strCurrentLongitude;
    }

    private void setStatusbar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    private void setInitLayout() {
        tvTitle = findViewById(R.id.tvTitle);
        tvInfo = findViewById(R.id.tvInfo);
        tvJumlahBarang = findViewById(R.id.tvJumlahBarang);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        tvKaos = findViewById(R.id.tvKaos);
        tvCelana = findViewById(R.id.tvCelana);
        tvJaket = findViewById(R.id.tvJaket);
        tvSprei = findViewById(R.id.tvSprei);
        tvKarpet = findViewById(R.id.tvKarpet);
        tvPriceKaos = findViewById(R.id.tvPriceKaos);
        tvPriceCelana = findViewById(R.id.tvPriceCelana);
        tvPriceJaket = findViewById(R.id.tvPriceJaket);
        tvPriceSprei = findViewById(R.id.tvPriceSprei);
        tvPriceKarpet = findViewById(R.id.tvPriceKarpet);
        imageAdd1 = findViewById(R.id.imageAdd1);
        imageAdd2 = findViewById(R.id.imageAdd2);
        imageAdd3 = findViewById(R.id.imageAdd3);
        imageAdd4 = findViewById(R.id.imageAdd4);
        imageAdd5 = findViewById(R.id.imageAdd5);
        imageMinus1 = findViewById(R.id.imageMinus1);
        imageMinus2 = findViewById(R.id.imageMinus2);
        imageMinus3 = findViewById(R.id.imageMinus3);
        imageMinus4 = findViewById(R.id.imageMinus4);
        imageMinus5 = findViewById(R.id.imageMinus5);
        btnCheckout = findViewById(R.id.btnCheckout);

        strTitle = getIntent().getExtras().getString(DATA_TITLE);
        if (strTitle != null) {
            tvTitle.setText(strTitle);
        }

        addDataViewModel = new ViewModelProvider(this, ViewModelProvider
                .AndroidViewModelFactory
                .getInstance(this.getApplication()))
                .get(AddDataViewModel.class);

        tvJumlahBarang.setText("0 items");
        tvTotalPrice.setText("Rp 0");
        tvInfo.setText("Premium Wash menawarkan treatment eksklusif, menggunakan chemical yang environment friendly dan service sepenuh hati.");
    }

    private void setItemData() {
        setItemDataHelper(tvKaos, imageAdd1, imageMinus1, tvPriceKaos, hargaKaos, 1);
        setItemDataHelper(tvCelana, imageAdd2, imageMinus2, tvPriceCelana, hargaCelana, 2);
        setItemDataHelper(tvJaket, imageAdd3, imageMinus3, tvPriceJaket, hargaJaket, 3);
        setItemDataHelper(tvSprei, imageAdd4, imageMinus4, tvPriceSprei, hargaSprei, 4);
        setItemDataHelper(tvKarpet, imageAdd5, imageMinus5, tvPriceKarpet, hargaKarpet, 5);
    }

    private void setItemDataHelper(TextView tvItem, ImageView imageAdd, ImageView imageMinus, TextView tvPrice, int harga, int itemIndex) {
        tvItem.setText(FunctionHelper.rupiahFormat(harga));

        imageAdd.setOnClickListener(v -> {
            increaseItemCount(itemIndex, tvPrice, harga);
        });

        imageMinus.setOnClickListener(v -> {
            decreaseItemCount(itemIndex, tvPrice, harga);
        });
    }

    private void increaseItemCount(int itemIndex, TextView tvPrice, int harga) {
        switch (itemIndex) {
            case 1:
                itemCount1++;
                tvPrice.setText(String.valueOf(itemCount1));
                countKaos = harga * itemCount1;
                break;
            case 2:
                itemCount2++;
                tvPrice.setText(String.valueOf(itemCount2));
                countCelana = harga * itemCount2;
                break;
            case 3:
                itemCount3++;
                tvPrice.setText(String.valueOf(itemCount3));
                countJaket = harga * itemCount3;
                break;
            case 4:
                itemCount4++;
                tvPrice.setText(String.valueOf(itemCount4));
                countSprei = harga * itemCount4;
                break;
            case 5:
                itemCount5++;
                tvPrice.setText(String.valueOf(itemCount5));
                countKarpet = harga * itemCount5;
                break;
        }
        setTotalPrice();
    }

    private void decreaseItemCount(int itemIndex, TextView tvPrice, int harga) {
        switch (itemIndex) {
            case 1:
                if (itemCount1 > 0) itemCount1--;
                tvPrice.setText(String.valueOf(itemCount1));
                countKaos = harga * itemCount1;
                break;
            case 2:
                if (itemCount2 > 0) itemCount2--;
                tvPrice.setText(String.valueOf(itemCount2));
                countCelana = harga * itemCount2;
                break;
            case 3:
                if (itemCount3 > 0) itemCount3--;
                tvPrice.setText(String.valueOf(itemCount3));
                countJaket = harga * itemCount3;
                break;
            case 4:
                if (itemCount4 > 0) itemCount4--;
                tvPrice.setText(String.valueOf(itemCount4));
                countSprei = harga * itemCount4;
                break;
            case 5:
                if (itemCount5 > 0) itemCount5--;
                tvPrice.setText(String.valueOf(itemCount5));
                countKarpet = harga * itemCount5;
                break;
        }
        setTotalPrice();
    }

    private void setTotalPrice() {
        totalItems = itemCount1 + itemCount2 + itemCount3 + itemCount4 + itemCount5;
        totalPrice = countKaos + countCelana + countJaket + countSprei + countKarpet;

        tvJumlahBarang.setText(totalItems + " items");
        tvTotalPrice.setText(FunctionHelper.rupiahFormat(totalPrice));
    }

    private void getCurrentLocation() {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addressList = geocoder.getFromLocation(strCurrentLatitude, strCurrentLongitude, 1);
            if (addressList != null && addressList.size() > 0) {
                strCurrentLocation = addressList.get(0).getLocality();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setInputData() {
        btnCheckout.setOnClickListener(v -> {
            if (totalItems == 0 || totalPrice == 0) {
                Toast.makeText(PremiumWashActivity.this, "Harap pilih jenis barang!", Toast.LENGTH_SHORT).show();
            } else {
                addDataViewModel.addDataLaundry(strTitle, totalItems, strCurrentLocation, totalPrice);
                Toast.makeText(PremiumWashActivity.this, "Pesanan Anda sedang diproses, cek di menu riwayat", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    public static void setWindowFlag(Activity activity, final int bits, boolean on) {
        Window window = activity.getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        if (on) {
            layoutParams.flags |= bits;
        } else {
            layoutParams.flags &= ~bits;
        }
        window.setAttributes(layoutParams);
    }
}
