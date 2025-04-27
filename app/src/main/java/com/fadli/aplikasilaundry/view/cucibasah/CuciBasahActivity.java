package com.fadli.aplikasilaundry.view.cucibasah;

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

public class CuciBasahActivity extends AppCompatActivity {

    public static final String DATA_TITLE = "TITLE";
    private static final int[] itemPrices = {7000, 5000, 8000, 55000, 150000}; // Prices for Kaos, Celana, Jaket, Sprei, Karpet
    private static final int[] itemCounts = new int[5]; // Count for each item
    private int totalItems, totalPrice;
    private String strTitle, strCurrentLocation, strCurrentLatLong;
    private double strCurrentLatitude, strCurrentLongitude;
    private SimpleLocation simpleLocation;
    private AddDataViewModel addDataViewModel;

    private TextView tvTitle, tvInfo, tvJumlahBarang, tvTotalPrice;
    private TextView[] tvItems = new TextView[5]; // Kaos, Celana, Jaket, Sprei, Karpet
    private TextView[] tvItemPrices = new TextView[5]; // Price for each item
    private ImageView[] imageAdd = new ImageView[5];
    private ImageView[] imageMinus = new ImageView[5];
    private Button btnCheckout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laundry);

        setLocation();
        setStatusbar();
        setInitLayout();
        setData();
        setInputData();
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

        tvItems[0] = findViewById(R.id.tvKaos);
        tvItems[1] = findViewById(R.id.tvCelana);
        tvItems[2] = findViewById(R.id.tvJaket);
        tvItems[3] = findViewById(R.id.tvSprei);
        tvItems[4] = findViewById(R.id.tvKarpet);

        tvItemPrices[0] = findViewById(R.id.tvPriceKaos);
        tvItemPrices[1] = findViewById(R.id.tvPriceCelana);
        tvItemPrices[2] = findViewById(R.id.tvPriceJaket);
        tvItemPrices[3] = findViewById(R.id.tvPriceSprei);
        tvItemPrices[4] = findViewById(R.id.tvPriceKarpet);

        imageAdd[0] = findViewById(R.id.imageAdd1);
        imageAdd[1] = findViewById(R.id.imageAdd2);
        imageAdd[2] = findViewById(R.id.imageAdd3);
        imageAdd[3] = findViewById(R.id.imageAdd4);
        imageAdd[4] = findViewById(R.id.imageAdd5);

        imageMinus[0] = findViewById(R.id.imageMinus1);
        imageMinus[1] = findViewById(R.id.imageMinus2);
        imageMinus[2] = findViewById(R.id.imageMinus3);
        imageMinus[3] = findViewById(R.id.imageMinus4);
        imageMinus[4] = findViewById(R.id.imageMinus5);

        btnCheckout = findViewById(R.id.btnCheckout);

        strTitle = getIntent().getExtras().getString(DATA_TITLE);
        if (strTitle != null) {
            tvTitle.setText(strTitle);
        }

        addDataViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication()))
                .get(AddDataViewModel.class);

        tvJumlahBarang.setText("0 items");
        tvTotalPrice.setText("Rp 0");
        tvInfo.setText("Cuci basah merupakan proses pencucian pakaian biasa menggunakan air dan deterjen.");
    }

    private void setData() {
        for (int i = 0; i < 5; i++) {
            tvItems[i].setText(FunctionHelper.rupiahFormat(itemPrices[i]));
            final int index = i;
            imageAdd[i].setOnClickListener(v -> {
                itemCounts[index]++;
                tvItemPrices[index].setText(String.valueOf(itemCounts[index]));
                updateTotalPrice();
            });
            imageMinus[i].setOnClickListener(v -> {
                if (itemCounts[index] > 0) {
                    itemCounts[index]--;
                    tvItemPrices[index].setText(String.valueOf(itemCounts[index]));
                }
                updateTotalPrice();
            });
        }
    }

    private void updateTotalPrice() {
        totalItems = 0;
        totalPrice = 0;
        for (int i = 0; i < 5; i++) {
            totalItems += itemCounts[i];
            totalPrice += itemCounts[i] * itemPrices[i];
        }
        tvJumlahBarang.setText(totalItems + " items");
        tvTotalPrice.setText(FunctionHelper.rupiahFormat(totalPrice));
    }

    private void getCurrentLocation() {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addressList = geocoder.getFromLocation(strCurrentLatitude, strCurrentLongitude, 1);
            if (addressList != null && !addressList.isEmpty()) {
                strCurrentLocation = addressList.get(0).getLocality();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setInputData() {
        btnCheckout.setOnClickListener(v -> {
            if (totalItems == 0 || totalPrice == 0) {
                Toast.makeText(CuciBasahActivity.this, "Harap pilih jenis barang!", Toast.LENGTH_SHORT).show();
            } else {
                addDataViewModel.addDataLaundry(strTitle, totalItems, strCurrentLocation, totalPrice);
                Toast.makeText(CuciBasahActivity.this, "Pesanan Anda sedang diproses, cek di menu riwayat", Toast.LENGTH_SHORT).show();
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
