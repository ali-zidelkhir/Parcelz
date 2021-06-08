package com.example.parcelz.Send;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.parcelz.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.shuhart.stepview.StepView;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;


public class Frame_C_Details extends AppCompatActivity
        implements OnMapReadyCallback {
    double Lat, Lang;
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
    double LATT, LONGG;

    RadioButton PriceBase, TimeBase, radioDelivery, radioPickup;
    TextView days, pricing;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frame_cdetails);
        stepView = findViewById(R.id.spb);

        // Retrieve location and camera position from saved instance state.
        if (savedInstanceState != null) {
            lastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            cameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    stepView.go(1, false);
                                }
                            }, 0
        );
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.google_map);
        mapFragment.getMapAsync(this);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
//get Last inserted Key
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
        Title = getIntent().getStringExtra("Title");
        H = getIntent().getStringExtra("H");
        W = getIntent().getStringExtra("W");
        L = getIntent().getStringExtra("L");
        Description = getIntent().getStringExtra("Description");
        Type = getIntent().getStringExtra("Type");
        LATT = getIntent().getDoubleExtra("LATT", 0.0);
        LONGG = getIntent().getDoubleExtra("LONGG", 0.0);

    }

    String Title, H, L, W, Description, Type;

    /**
     * Saves the state of the map when the activity is paused.
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (map != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, map.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, lastKnownLocation);
        }
        super.onSaveInstanceState(outState);
    }


    /**
     * Manipulates the map when it's available.
     * This callback is triggered when the map is ready to be used.
     */
    String StreetAddress;
    String x;
    String y;
    String z;
    String w;

    @Override
    public void onMapReady(GoogleMap map) {
        this.map = map;
        getLocationPermission();
        updateLocationUI();
        getDeviceLocation();
        LatLng sydney = new LatLng(-33.852, 151.211);
        map.addMarker(new MarkerOptions()
                .position(sydney)
                .title("Marker in Sydney"));
        map.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        // Prompt the user for permission.
        map.animateCamera(CameraUpdateFactory.zoomIn());
        map.animateCamera(CameraUpdateFactory.zoomOut());
        try {
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(lastKnownLocation.getLatitude(),
                            lastKnownLocation.getLongitude()), DEFAULT_ZOOM));
        } catch (Exception e) {

        }
        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                //markerOptions.title(latLng.latitude + " : " + latLng.longitude);
                map.clear();
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
                map.addMarker(markerOptions);
                try {
                    Lat = latLng.latitude;
                    Lang = latLng.longitude;
                    List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                    if (addresses.size() > 0) {
                        Address address = addresses.get(0);
                        StreetAddress = address.getAddressLine(0);
                        x = address.getAddressLine(0);
                        y = address.getLocality();
                        z = address.getAdminArea();
                        w = address.getSubLocality();
                        Toast.makeText(Frame_C_Details.this,
                                "AAAAA:" + x + "\n" + y + "\n" + z + "\n" + w,
                                Toast.LENGTH_SHORT).show();
                        map.addMarker(new MarkerOptions().position(latLng).title(StreetAddress));
                    }
                } catch (Exception r) {

                }


            }
        });
    }

    /**
     * Gets the current location of the device, and positions the map's camera.
     */
    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (locationPermissionGranted) {
                Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            lastKnownLocation = task.getResult();
                            if (lastKnownLocation != null) {
                                map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                        new LatLng(lastKnownLocation.getLatitude(),
                                                lastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                            }
                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            map.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(defaultLocation, DEFAULT_ZOOM));
                            map.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage(), e);
        }
    }

    /**
     * Prompts the user for permission to use the device location.
     */
    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    /**
     * Handles the result of the request for location permissions.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        locationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
    }


    private void updateLocationUI() {
        if (map == null) {
            return;
        }
        try {
            if (locationPermissionGranted) {
                map.setMyLocationEnabled(true);
                map.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                map.setMyLocationEnabled(false);
                map.getUiSettings().setMyLocationButtonEnabled(false);
                lastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void updateLocation() {

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        rootRef.child("Send_Details_A")
                .child(Key)
                .child("latitude_destination")
                .setValue(Lat);
        rootRef.child("Send_Details_A")
                .child(Key)
                .child("longitude_destination")
                .setValue(Lang);
        try {
            Geocoder gcd = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = gcd.getFromLocation(Lat, Lang, 1);
            if (addresses.size() > 0) {

                String locality = addresses.get(0).getLocality(); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                String subLocality = addresses.get(0).getSubLocality(); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                String address0 = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                String address1 = addresses.get(0).getAddressLine(1); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                String address2 = addresses.get(0).getAddressLine(2); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName();

                rootRef.child("Send_Details_A")
                        .child(Key)
                        .child("willaya")
                        .setValue(state);
                rootRef.child("Send_Details_A")
                        .child(Key)
                        .child("baladia")
                        .setValue(locality);
            } else {
                // do your stuff
            }
        } catch (Exception e) {

        }

        Toast.makeText(Frame_C_Details.this, "Update LATLANG AVEC Success", Toast.LENGTH_SHORT).show();
    }

    double dist = 0;

    public void Next(View view) {
        /*try {
            Geocoder gcd = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = gcd.getFromLocation(Lat, Lang, 1);
            if (addresses.size() > 0) {

                String locality = addresses.get(0).getLocality(); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                String subLocality = addresses.get(0).getSubLocality(); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                String address0 = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                String address1 = addresses.get(0).getAddressLine(1); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                String address2 = addresses.get(0).getAddressLine(2); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName();


                System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx 1" + locality);//baladia
                System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx 2" + subLocality);//street name big
                System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx 3" + address0);//rue name
                System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx 4" + address1);
                System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx 5" + address2);
                System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx 6" + city);//baladia
                System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx 7" + state);//willaya province
                System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx 8" + country);//algeria
                System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx 9" + postalCode);
                System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx 0" + knownName);

                System.out.println("AAAAAAAAAAAAAAAA " + x + "AAAAAAAAAAAAAAAA " + y + "AAAAAAAAAAAAAAAA " + z + "AAAAAAAAAAAAAAAA " + w);
            } else {
                // do your stuff
            }
        } catch (Exception e) {
        }*/
        updateLocation();
        Intent mainI = new Intent(Frame_C_Details.this, Frame_D_Details.class);
        mainI.putExtra("LATTDEST", Lat);
        mainI.putExtra("LONGGDEST", Lang);
        mainI.putExtra("LATT", LATT);
        mainI.putExtra("LONGG", LONGG);
        mainI.putExtra("dist", dist);
        mainI.putExtra("Key", Key);
        mainI.putExtra("Title", Title );
        mainI.putExtra("H", H );
        mainI.putExtra("L", L );
        mainI.putExtra("W", W );
        mainI.putExtra("Description", Description);
        mainI.putExtra("Type", Type);
        startActivity(mainI);
        finish();

    }
}
