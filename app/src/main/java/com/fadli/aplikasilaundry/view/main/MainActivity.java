package com.fadli.aplikasilaundry.view.main;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fadli.aplikasilaundry.R;
import com.fadli.aplikasilaundry.view.cucibasah.CuciBasahActivity;
import com.fadli.aplikasilaundry.view.dryclean.DryCleanActivity;
import com.fadli.aplikasilaundry.view.history.HistoryActivity;
import com.fadli.aplikasilaundry.view.ironing.IroningActivity;
import com.fadli.aplikasilaundry.view.premiumwash.PremiumWashActivity;
import com.fadli.aplikasilaundry.viewmodel.MainViewModel;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;

import java.util.ArrayList;
import java.util.List;

import im.delight.android.location.SimpleLocation;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final int REQ_PERMISSION = 100;
    private double strCurrentLatitude;
    private double strCurrentLongitude;
    private String strCurrentLocation;
    private GoogleMap mapsView;
    private SimpleLocation simpleLocation;
    private ProgressDialog progressDialog;
    private MainViewModel mainViewModel;
    private MenuAdapter menuAdapter;
    private MainAdapter mainAdapter;
    private RecyclerView rvMenu, rvRekomendasi;
    private LinearLayout layoutHistory;
    private List<ModelMenu> modelMenuList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setStatusBarTransparent();
        checkLocationPermission();
        initializeLocation();
        initializeViews();
        setupMenu();
        loadLocationData();
    }

    private void initializeLocation() {
        simpleLocation = new SimpleLocation(this);

        if (!simpleLocation.hasLocationEnabled()) {
            SimpleLocation.openSettings(this);
        }

        strCurrentLatitude = simpleLocation.getLatitude();
        strCurrentLongitude = simpleLocation.getLongitude();
        strCurrentLocation = strCurrentLatitude + "," + strCurrentLongitude;
    }

    private void initializeViews() {
        rvMenu = findViewById(R.id.rvMenu);
        rvRekomendasi = findViewById(R.id.rvRekomendasi);
        layoutHistory = findViewById(R.id.layoutHistory);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Mohon Tunggu…");
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Sedang menampilkan lokasi...");

        rvMenu.setLayoutManager(new GridLayoutManager(this, 2));
        rvMenu.setHasFixedSize(true);

        mainAdapter = new MainAdapter(this);
        rvRekomendasi.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvRekomendasi.setAdapter(mainAdapter);
        rvRekomendasi.setHasFixedSize(true);

        layoutHistory.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, HistoryActivity.class)));
    }

    private void checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQ_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQ_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                recreate();
            }
        }
    }

    private void setStatusBarTransparent() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            );
        }
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    private void setupMenu() {
        modelMenuList.add(new ModelMenu("Cuci Basah", R.drawable.ic_cuci_basah));
        modelMenuList.add(new ModelMenu("Dry Cleaning", R.drawable.ic_dry_cleaning));
        modelMenuList.add(new ModelMenu("Premium Wash", R.drawable.ic_premium_wash));
        modelMenuList.add(new ModelMenu("Setrika", R.drawable.ic_setrika));

        menuAdapter = new MenuAdapter(this, modelMenuList);
        rvMenu.setAdapter(menuAdapter);

        menuAdapter.setOnItemClickListener(modelMenu -> {
            Intent intent = null;
            switch (modelMenu.getTvTitle()) {
                case "Cuci Basah":
                    intent = new Intent(this, CuciBasahActivity.class);
                    break;
                case "Dry Cleaning":
                    intent = new Intent(this, DryCleanActivity.class);
                    break;
                case "Premium Wash":
                    intent = new Intent(this, PremiumWashActivity.class);
                    break;
                case "Setrika":
                    intent = new Intent(this, IroningActivity.class);
                    break;
            }
            if (intent != null) {
                intent.putExtra("DATA_TITLE", modelMenu.getTvTitle());
                startActivity(intent);
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

    private void loadLocationData() {
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        mainViewModel.setMarkerLocation(strCurrentLocation);
        progressDialog.show();
        mainViewModel.getMarkerLocation().observe(this, modelResults -> {
            if (modelResults != null && !modelResults.isEmpty()) {
                mainAdapter.setLocationAdapter(modelResults);
            }
            progressDialog.dismiss();
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.mapsView = googleMap;
        loadLocationData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_PERMISSION && resultCode == RESULT_OK) {
            loadLocationData();
        }
    }
}
