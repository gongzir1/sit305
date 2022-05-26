package com.example.trucksharingapp;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.trucksharingapp.databinding.ActivityMapsBinding;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    Button call,book;
    static int PERMISSION_CODE= 100;
    TextView pickup,dropoff,time,money;
    Integer distance=100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map12);
        mapFragment.getMapAsync(this);
        Intent intent=getIntent();
        String from=intent.getStringExtra("from");
        String to=intent.getStringExtra("to");
        call=findViewById(R.id.calldriver);
        book=findViewById(R.id.book);
        pickup=findViewById(R.id.pickup);
        dropoff=findViewById(R.id.dropOff);
        time=findViewById(R.id.time);
        money=findViewById(R.id.fare);
        pickup.setText("Pick up at: "+from);
        dropoff.setText("Drop off at: "+to);

        time.setText("approxi time is: "+distance/10);
        money.setText("approxi fare is: "+distance*1.5);




        if (ContextCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(MapsActivity.this,new String[]{Manifest.permission.CALL_PHONE},PERMISSION_CODE);

        }

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String phoneno = "123321";
                Intent i = new Intent(Intent.ACTION_CALL);
                i.setData(Uri.parse("tel:"+phoneno));
                startActivity(i);

            }
        });
        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MapsActivity.this,"successfully book!",Toast.LENGTH_SHORT).show();
                Intent intent1=new Intent(getApplicationContext(),CheckoutActivity.class);
                startActivity(intent1);

            }
        });
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //deakin -37.8585803,145.109048
        //city -37.971237,144.4926947
        // Add a marker in Sydney and move the camera
        LatLng deakin = new LatLng(-37.8585803, 145.109048);
        LatLng city = new LatLng(-37.971237, 144.4926947);
        mMap.addMarker(new MarkerOptions().position(deakin).title("Marker in deakin"));
        mMap.addMarker(new MarkerOptions().position(city).title("Marker in city"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(deakin));


        
    }

}