package com.fadli.aplikasilaundry.view.dryclean;

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

public class DryCleanActivity extends AppCompatActivity {

    public static final String DATA_TITLE = "TITLE";
    private final int[] prices = {8000, 6000, 9000, 65000, 200000};  // Kaos, Celana, Jaket, Sprei, Karpet
    private final int[] itemCounts = new int[5];
    private final TextView[] priceTextViews = new TextView[5];
    private final ImageView[] addButtons = new ImageView[5];
    private final ImageView[] minusButtons = new ImageView[5];
    private final TextView[] itemTextViews = new TextView[5];

    private int totalItems, totalPrice;
    private String strTitle, strCurrentLocation, strCurrentLatLong;
    private double strCurrentLatitude, strCurrentLongitude;
    private SimpleLocation simpleLocation;
    private AddDataViewModel addDataViewModel;
    private Button btnCheckout;
    private TextView tvTitle, tvInfo, tvJumlahBarang, tvTotalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laundry);

        setLocation();
        setStatusbar();
        setInitLayout();
        setInputData();
        getCurrentLocation();
    }

    private void setLocation() {
        simpleLocation = new SimpleLocation(this);

        if (!simpleLocation.hasLocationEnabled()) {
            SimpleLocation.openSettings(this);
        }

        // Get location
        strCurrentLatitude = simpleLocation.getLatitude();
        strCurrentLongitude = simpleLocation.getLongitude();

        // Set location lat long
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

        itemTextViews[0] = findViewById(R.id.tvKaos);
        itemTextViews[1] = findViewById(R.id.tvCelana);
        itemTextViews[2] = findViewById(R.id.tvJaket);
        itemTextViews[3] = findViewById(R.id.tvSprei);
        itemTextViews[4] = findViewById(R.id.tvKarpet);

        priceTextViews[0] = findViewById(R.id.tvPriceKaos);
        priceTextViews[1] = findViewById(R.id.tvPriceCelana);
        priceTextViews[2] = findViewById(R.id.tvPriceJaket);
        priceTextViews[3] = findViewById(R.id.tvPriceSprei);
        priceTextViews[4] = findViewById(R.id.tvPriceKarpet);

        addButtons[0] = findViewById(R.id.imageAdd1);
        addButtons[1] = findViewById(R.id.imageAdd2);
        addButtons[2] = findViewById(R.id.imageAdd3);
        addButtons[3] = findViewById(R.id.imageAdd4);
        addButtons[4] = findViewById(R.id.imageAdd5);

        minusButtons[0] = findViewById(R.id.imageMinus1);
        minusButtons[1] = findViewById(R.id.imageMinus2);
        minusButtons[2] = findViewById(R.id.imageMinus3);
        minusButtons[3] = findViewById(R.id.imageMinus4);
        minusButtons[4] = findViewById(R.id.imageMinus5);

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
        tvInfo.setText("Cuci kering adalah proses pencucian pakaian menggunakan bahan kimia dan teknik tertentu tanpa air.");

        for (int i = 0; i < 5; i++) {
            itemTextViews[i].setText(FunctionHelper.rupiahFormat(prices[i]));
            final int index = i;
            setItemButtons(index);
        }
    }

    private void setItemButtons(int index) {
        addButtons[index].setOnClickListener(v -> {
            itemCounts[index]++;
            priceTextViews[index].setText(String.valueOf(itemCounts[index]));
            setTotalPrice();
        });

        minusButtons[index].setOnClickListener(v -> {
            if (itemCounts[index] > 0) {
                itemCounts[index]--;
                priceTextViews[index].setText(String.valueOf(itemCounts[index]));
            }
            setTotalPrice();
        });
    }

    private void setTotalPrice() {
        totalItems = 0;
        totalPrice = 0;
        for (int i = 0; i < 5; i++) {
            totalItems += itemCounts[i];
            totalPrice += prices[i] * itemCounts[i];
        }

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
                Toast.makeText(DryCleanActivity.this, "Harap pilih jenis barang!", Toast.LENGTH_SHORT).show();
            } else {
                addDataViewModel.addDataLaundry(strTitle, totalItems, strCurrentLocation, totalPrice);
                Toast.makeText(DryCleanActivity.this, "Pesanan Anda sedang diproses, cek di menu riwayat", Toast.LENGTH_SHORT).show();
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
