package com.example.trucksharingapp;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.trucksharingapp.model.JsonRootBean;
import com.example.trucksharingapp.model.Results;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MapActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private final int Time = 20000;
    double latitude = 0;
    double longitude = 0;
    ImageView ivPosition;
    Button btnStop;
    int REQUEST_CODE = 483;


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (latitude != 0 && longitude != 0) {
            LatLng PERTH = new LatLng(latitude, longitude);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(PERTH, 15));
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_activity);
        PermissionHelper permissionHelper = new PermissionHelper();
        permissionHelper.requestLocationPermission(this);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
        } else {
            Toast.makeText(this, "Failed to obtain location permission.", Toast.LENGTH_SHORT).show();
        }
        initUI();
        initPermission();
    }

    /**
     * Get location permission
     */
    private void initPermission() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ActivityCompat.requestPermissions(MapActivity.this, new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,

                }, 11);
            }
        }, 1000);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PERMISSION_GRANTED) {
                // Check permission status
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                    /*
                      If the user completely refuses to grant permission, the user is usually prompted to go to the Set Permissions
                      screen After the first failed grant, when exiting the app and re-entering, the Allow Permissions
                      prompt will be brought up again here
                     */
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_BACKGROUND_LOCATION,}, 102);
                } else {
                    /*
                      The user does not completely refuse to grant permissions the first time he installs it,
                      the permission prompt box is called up, and then it never prompts again.
                     */
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_BACKGROUND_LOCATION,}, 102);
                }
            } else {
                getMyLocation();
            }
        }
    }

    @SuppressLint("MissingPermission")
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void getMyLocation() {
        // Get user location
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 10, new Listener());
        // Have another for GPS provider just in case.
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10, new Listener());
        // Try to request the location immediately
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if (location == null) {
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }
        if (location != null) {

            latitude = location.getLatitude();
            longitude = location.getLongitude();
            SupportMapFragment mapFragment = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fmap));
            mapFragment.getMapAsync(this);


            LatLng PERTH = new LatLng(latitude, longitude);
            if (mMap != null)
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(PERTH, 15));
        }
    }

    /**
     * Handle lat lng.
     */
    private void handleLatLng(double latitudevalue, double longitudevalue) {
        latitude = latitudevalue;
        longitude = longitudevalue;
    }

    private PlacesClient placesClient;

    private class Listener implements LocationListener {
        public void onLocationChanged(Location location) {
            double longitude = location.getLongitude();
            double latitude = location.getLatitude();
            handleLatLng(latitude, longitude);
        }

        public void onProviderDisabled(String provider) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PERMISSION_GRANTED) {
            } else {
                Toast.makeText(this, "Failed to obtain storage permission.", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == 102) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getMyLocation();
            }
        }
    }

    private void initUI() {
        ivPosition = findViewById(R.id.ivPosition);
        ivPosition.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View view) {
                LatLng PERTH = new LatLng(latitude, longitude);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(PERTH, 15));
            }
        });
        Places.initialize(getApplicationContext(), "AIzaSyAQ4ZDGyQlHdzmY28GwFEG_JxtPFQ59sAU");
        placesClient = Places.createClient(this);
        btnStop = findViewById(R.id.btnStop);
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MapActivity.this, "In the process of acquiring network place names", Toast.LENGTH_LONG).show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        toBack();
                    }
                }).start();
            }
        });
    }

    private void toBack() {
        Map<String, String> map = new HashMap<>();
        map.put("latlng", latitude + "," + longitude);
        map.put("key", "AIzaSyAob3UhwV7GNSSPzd0Szr7-XYziIRNizrc");
        HttpUtils.getRequest("https://maps.googleapis.com/maps/api/geocode/json", map, "utf-8", new OnResponseListener() {
            @Override
            public void onSuccess(String response) {
                System.out.println(response);
                JsonRootBean result = new Gson().fromJson(response, JsonRootBean.class);
                if (result.getStatus().equals("OK")) {
                    List<Results> resultss = result.getResults();
                    Intent intent = new Intent();
                    if (resultss.size() > 0)
                        intent.putExtra("addressName", resultss.get(0).getFormatted_address());
                    else
                        intent.putExtra("addressName", "");
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MapActivity.this, result.getStatus(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }

            @Override
            public void onError(String error) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MapActivity.this, error, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}