package com.example.parcelz.Send;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.FloatMath;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.parcelz.R;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.SphericalUtil;
import com.shuhart.stepview.StepView;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.List;

public class Frame_D_Details extends AppCompatActivity {
    String Key = "";
    private static final String TAG = Frame_C_Details.class.getSimpleName();
    private GoogleMap map;
    private CameraPosition cameraPosition;
    DatabaseReference databaseReference;
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    Geocoder geocoder;
    Switch switchFrangible;
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
    RadioButton PriceBase, TimeBase, radioDelivery, radioPickup;
    TextView days, pricing;

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

        switchFrangible = findViewById(R.id.switchFrangible);
        days = findViewById(R.id.days);
        pricing = findViewById(R.id.pricing);
        PriceBase = findViewById(R.id.PriceBase);
        TimeBase = findViewById(R.id.TimeBase);
        radioDelivery = findViewById(R.id.radioDelivery);
        radioPickup = findViewById(R.id.radioPickup);


        stepView = findViewById(R.id.spb);
        LATT = getIntent().getDoubleExtra("LATT", 0.0);
        dist = getIntent().getDoubleExtra("dist", -1);
        System.out.println("diiiiiiiiiiiiiiiiiist::::::::" + dist);
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
        PriceBase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetPrice();
            }
        });
        TimeBase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetPrice();
            }
        });
        radioDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetPrice();
            }
        });
        radioPickup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetPrice();
            }
        });
        switchFrangible.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                GetPrice();
            }
        });


        LATT = getIntent().getDoubleExtra("LATT", 0.0);
        LONGG = getIntent().getDoubleExtra("LONGG", 0.0);
        LATTDEST = getIntent().getDoubleExtra("LATTDEST", 0.0);
        LONGGDEST = getIntent().getDoubleExtra("LONGGDEST", 0.0);
        Title = getIntent().getStringExtra("Title");
        H = getIntent().getStringExtra("H");
        W = getIntent().getStringExtra("W");
        L = getIntent().getStringExtra("L");
        Description = getIntent().getStringExtra("Description");
        Type = getIntent().getStringExtra("Type");
        LatLng i = new LatLng(LATT, LONGG);
        LatLng j = new LatLng(LATTDEST, LONGGDEST);
        CalculationByDistance(i, j);
        System.out.println("|||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
        System.out.println("my distance will be this one:" + dist);
        GetPrice();
    }

    String Title, H, L, W, Description, Type;

    double dist = 0;

    public double CalculationByDistance(LatLng StartP, LatLng EndP) {

        int Radius = 6371;// radius of earth in Km
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult = Radius * c;
        double km = valueResult / 1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec = Integer.valueOf(newFormat.format(km));
        double meter = valueResult % 1000;
        int meterInDec = Integer.valueOf(newFormat.format(meter));
        System.out.println("Radius Value" + valueResult + "   KM  " + kmInDec + " Meter   " + meterInDec);
        dist = Radius * c;
        return Radius * c;
    }

    public double GetPrice() {
        System.out.println("GetPRice*******************:" + W);
        System.out.println("GetPRice*******************:" + H);
        System.out.println("GetPRice*******************:" + L);
        System.out.println("GetPRice*******************:" + dist);


        double x = dist ;
        double p1 = 0, p2 = 0, p3 = 0, p4 = 0;
        double w, h, l, tot = 0;
        w = Double.parseDouble(W);
        h = Double.parseDouble(H);
        l = Double.parseDouble(L);
        tot = w * 0.9 + h * 0.9 + l * 0.9;
        //si le produit est fragile
        if (switchFrangible.isChecked()) {
            p1 = x * 0.3; //100DA
        }
        //delivery mode
        if (TimeBase.isChecked()) {
            p2 = x * 0.5;
        }
        if (PriceBase.isChecked()) {
            p2 = 0;
        }
        //

        double total = x * 0.75 + p1 + p2 + tot;
        pricing.setText(String.format("%.2f", total) + " DA");
        return total;
    }


    private LatLng pickupLocation;

    private void updateLocation() {

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        if (PriceBase.isChecked()) {
            rootRef.child("Send_Details_A")
                    .child(Key)
                    .child("Delivery_mode")
                    .setValue("Price Base");
        }

        if (TimeBase.isChecked()) {
            rootRef.child("Send_Details_A")
                    .child(Key)
                    .child("Delivery_mode")
                    .setValue("Time Base");
        }

        if (radioDelivery.isChecked()) {
            rootRef.child("Send_Details_A")
                    .child(Key)
                    .child("cash_on")
                    .setValue("Delivery");
        }

        if (radioPickup.isChecked()) {
            rootRef.child("Send_Details_A")
                    .child(Key)
                    .child("cash_on")
                    .setValue("Pickup");
        }

        if (switchFrangible.isChecked()) {
            rootRef.child("Send_Details_A")
                    .child(Key)
                    .child("frangible")
                    .setValue("frangible");
        } else {
            rootRef.child("Send_Details_A")
                    .child(Key)
                    .child("frangible")
                    .setValue("no");
        }
        rootRef.child("Send_Details_A")
                .child(Key)
                .child("total")
                .setValue(GetPrice());
        Toast.makeText(Frame_D_Details.this, "Update LATLANG AVEC Success", Toast.LENGTH_SHORT).show();
    }

    public void Next(View view) {
        try {
            try {
                LatLng i = new LatLng(LATT, LONGG);
                LatLng j = new LatLng(LATTDEST, LONGGDEST);
                CalculationByDistance(i, j);
            } catch (Exception e) {
            }

            updateLocation();
            String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("custumerRequest");
            //GeoFire geoFire = new GeoFire(ref);
            //geoFire.setLocation(userID, new GeoLocation(LATT, LONGG));
            //pickupLocation = new LatLng(LATTDEST, LONGGDEST);
            //String key = geoFire.getDatabaseReference().push().getKey();
           /* geoFire.setLocation(key, new GeoLocation(LATT, LONGG), new GeoFire.CompletionListener() {
                @Override
                public void onComplete(String key, DatabaseError error) {
                    System.out.println("DDDDDDDDDDDDDDDDDDDDDDDDDDDDDDD");
                }
            });*/

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}


