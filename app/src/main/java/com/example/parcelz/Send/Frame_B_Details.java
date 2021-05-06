package com.example.parcelz.Send;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.parcelz.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.shuhart.stepview.StepView;

import org.jetbrains.annotations.NotNull;

public class Frame_B_Details extends AppCompatActivity {
    SupportMapFragment mapFragment;
    FusedLocationProviderClient client;
    StepView stepView;
    int PLACE_PICKER_REQUEST = 111;
    int stepIndex = 1;
    String Key="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frame_bdetails);
        Key=getIntent().getStringExtra("Key");

        //assign variable
        stepView = findViewById(R.id.spb);
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.google_map);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    stepView.go(1, false);
                                }
                            }, 0
        );

        //initialize fused
        client = LocationServices.getFusedLocationProviderClient(Frame_B_Details.this);

        //check permission GPS Activation
        if (ActivityCompat.checkSelfPermission(Frame_B_Details.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation();
        } else {
            ActivityCompat.requestPermissions(
                    Frame_B_Details.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PLACE_PICKER_REQUEST);
        }
    }


    public void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation();
            // return;
        }
        Task<Location> task = client.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    mapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(@NonNull @NotNull GoogleMap googleMap) {
                            //initialize LATLNG
                            googleMap.clear();
                            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                            //create marker option
                            MarkerOptions options = new MarkerOptions().position(latLng).title("I am Here");
                            //zoom map
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));
                            //add marker on map
                            googleMap.addMarker(options).showInfoWindow();
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            }
        } else {
            Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
        }
    }

    public void Next(View view) {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    stepIndex++;
                                    stepView.go(stepIndex, true);
                                    Intent mainI = new Intent(Frame_B_Details.this, Frame_C_Details.class);
                                    startActivity(mainI);
                                    finish();
                                }
                            }, 3000
        );
    }
}