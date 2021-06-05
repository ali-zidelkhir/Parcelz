package com.example.parcelz.Send;

import androidx.appcompat.app.AppCompatActivity;

import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.FloatMath;
import android.view.View;

import com.example.parcelz.R;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.SphericalUtil;
import com.shuhart.stepview.StepView;

import java.util.List;

public class Frame_D_Details extends AppCompatActivity {
    String Key = "";
    private static final String TAG = Frame_C_Details.class.getSimpleName();
    private GoogleMap map;
    private CameraPosition cameraPosition;
    DatabaseReference databaseReference;
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    Geocoder geocoder;

    // The entry point to the Fused Location Provider.
    private FusedLocationProviderClient fusedLocationProviderClient;

    // A default location (Sydney, Australia) and default zoom to use when location permission is
    // not granted.
    private final LatLng defaultLocation = new LatLng(-33.8523341, 151.2106085);
    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean locationPermissionGranted;

    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private Location lastKnownLocation;
    // Keys for storing activity state.
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";

    // Used for selecting the current place.
    private static final int M_MAX_ENTRIES = 5;
    private String[] likelyPlaceNames;
    private String[] likelyPlaceAddresses;
    private List[] likelyPlaceAttributions;
    private LatLng[] likelyPlaceLatLngs;
    StepView stepView;
    double LATT, LONGG, LATTDEST, LONGGDEST;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frame_ddetails);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    stepView.go(2, false);
                                }
                            }, 0
        );
        LATT = getIntent().getDoubleExtra("LATT", 0.0);
        LONGG = getIntent().getDoubleExtra("LONGG", 0.0);
        LATTDEST = getIntent().getDoubleExtra("LATTDEST", 0.0);
        LONGGDEST = getIntent().getDoubleExtra("LONGGDEST", 0.0);

        calcDistance();


        stepView = findViewById(R.id.spb);
        LATT = getIntent().getDoubleExtra("LATT", 0.0);
        LONGG = getIntent().getDoubleExtra("LONGG", 0.0);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Send_Details_A");
        Query query = databaseReference.orderByKey().limitToLast(1);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    Key = (childSnapshot.getKey());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });
    }

    double xxxxx = (double) 0.0;

    private void calcDistance() {
        LatLng sydney = new LatLng(LATT, LONGG);
        LatLng Brisbane = new LatLng(LATTDEST, LONGGDEST);
        Double distance, distance2;
        distance = SphericalUtil.computeDistanceBetween(sydney, Brisbane);
        distance2 = SphericalUtil.computeDistanceBetween(Brisbane, sydney);
        xxxxx = distance / 1000;
        System.out.println("OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO>>>>> : " + String.format("%.2f", distance / 1000));
        System.out.println("OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO>>>>> : " + String.format("%.2f", distance2 / 1000));
    }

    private LatLng pickupLocation;

    public void Next(View view) {
        try {
            String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("custumerRequest");
            GeoFire geoFire = new GeoFire(ref);
            //geoFire.setLocation(userID, new GeoLocation(LATT, LONGG));
            //pickupLocation = new LatLng(LATTDEST, LONGGDEST);


            String key = geoFire.getDatabaseReference().push().getKey();
            geoFire.setLocation(key, new GeoLocation(LATT, LONGG), new GeoFire.CompletionListener() {
                @Override
                public void onComplete(String key, DatabaseError error) {
                    System.out.println("DDDDDDDDDDDDDDDDDDDDDDDDDDDDDDD");
                }
            });

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}


