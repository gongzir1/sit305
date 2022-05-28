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

import com.example.trucksharingapp.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.Dot;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.RoundCap;

import java.util.Arrays;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleMap.OnPolylineClickListener{

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    Button call,book;
    static int PERMISSION_CODE= 100;
    TextView pickup,dropoff,time,money;
    Integer distance=100;
    Polyline currentPolyline;
    MarkerOptions place1,place2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map1111123);
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
        //city -37.8274646,144.8985064
        // Add a marker in Sydney and move the camera
        LatLng deakin = new LatLng(-37.8585803, 145.109048);
        LatLng city = new LatLng(-37.8274646, 144.8985064);
        mMap.addMarker(new MarkerOptions().position(deakin).title("Marker in deakin"));
        mMap.addMarker(new MarkerOptions().position(city).title("Marker in city"));
        place1=new MarkerOptions().position(deakin).title("deakin");
        place2=new MarkerOptions().position(city).title("city");

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(deakin, 9));





        Polyline polyline1 = googleMap.addPolyline(new PolylineOptions()
                .clickable(true)
                .add(
//                        new LatLng(-35.016, 143.321),
//                        new LatLng(-34.747, 145.592),
//                        new LatLng(-34.364, 147.891),
//                        new LatLng(-33.501, 150.217),
                        new LatLng(-37.8585803, 145.109048),
                        new LatLng(-37.8274646, 144.8985064)));
        // Store a data object with the polyline, used here to indicate an arbitrary type.
        polyline1.setTag("A");
        // Style the polyline.
        stylePolyline(polyline1);

//        Polyline polyline2 = googleMap.addPolyline(new PolylineOptions()
//                .clickable(true)
//                .add(
//                        new LatLng(-29.501, 119.700),
//                        new LatLng(-27.456, 119.672),
//                        new LatLng(-25.971, 124.187),
//                        new LatLng(-28.081, 126.555),
//                        new LatLng(-28.848, 124.229),
//                        new LatLng(-28.215, 123.938)));
//        polyline2.setTag("B");
       // stylePolyline(polyline2);

        // Add polygons to indicate areas on the map.
//        Polygon polygon1 = googleMap.addPolygon(new PolygonOptions()
//                .clickable(true)
//                .add(
//                        new LatLng(-27.457, 153.040),
//                        new LatLng(-33.852, 151.211),
//                        new LatLng(-37.813, 144.962),
//                        new LatLng(-34.928, 138.599)));
//        // Store a data object with the polygon, used here to indicate an arbitrary type.
//        polygon1.setTag("alpha");
//        // Style the polygon.
//        stylePolygon(polygon1);
//
//        Polygon polygon2 = googleMap.addPolygon(new PolygonOptions()
//                .clickable(true)
//                .add(
//                        new LatLng(-31.673, 128.892),
//                        new LatLng(-31.952, 115.857),
//                        new LatLng(-17.785, 122.258),
//                        new LatLng(-12.4258, 130.7932)));
//        polygon2.setTag("beta");
//        stylePolygon(polygon2);

        // Position the map's camera near Alice Springs in the center of Australia,
        // and set the zoom factor so most of Australia shows on the screen.
      //  googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-23.684, 133.903), 4));

        // Set listeners for click events.
        googleMap.setOnPolylineClickListener(this);
       // googleMap.setOnPolygonClickListener(this);
    }

    private static final int COLOR_BLACK_ARGB = 0xff000000;
    private static final int POLYLINE_STROKE_WIDTH_PX = 12;

    /**
     * Styles the polyline, based on type.
     * @param polyline The polyline object that needs styling.
     */
    private void stylePolyline(Polyline polyline) {
        String type = "";
        // Get the data object stored with the polyline.
        if (polyline.getTag() != null) {
            type = polyline.getTag().toString();
        }

        switch (type) {
            // If no type is given, allow the API to use the default.
            case "A":
//            case "B":
                // Use a round cap at the start of the line.
                // Use a custom bitmap as the cap at the start of the line.
                polyline.setStartCap(new RoundCap());
                //polyline.setStartCap(
//                       // new CustomCap(
                            //    BitmapDescriptorFactory.fromResource(R.drawable.ic_arrow), 10));

                break;
        }

        polyline.setEndCap(new RoundCap());
        polyline.setWidth(POLYLINE_STROKE_WIDTH_PX);
        polyline.setColor(COLOR_BLACK_ARGB);
        polyline.setJointType(JointType.ROUND);
    }

    private static final int PATTERN_GAP_LENGTH_PX = 20;
    private static final PatternItem DOT = new Dot();
    private static final PatternItem GAP = new Gap(PATTERN_GAP_LENGTH_PX);

    // Create a stroke pattern of a gap followed by a dot.
    private static final List<PatternItem> PATTERN_POLYLINE_DOTTED = Arrays.asList(GAP, DOT);

    /**
     * Listens for clicks on a polyline.
     * @param polyline The polyline object that the user has clicked.
     */
    @Override
    public void onPolylineClick(Polyline polyline) {
        // Flip from solid stroke to dotted stroke pattern.
        if ((polyline.getPattern() == null) || (!polyline.getPattern().contains(DOT))) {
            polyline.setPattern(PATTERN_POLYLINE_DOTTED);
        } else {
            // The default pattern is a solid stroke.
            polyline.setPattern(null);
        }

        Toast.makeText(this, "Route type " + polyline.getTag().toString(),
                Toast.LENGTH_SHORT).show();
    }

    /**
     * Listens for clicks on a polygon.
     * @param polygon The polygon object that the user has clicked.
     */
    //   @Override
//    public void onPolygonClick(Polygon polygon) {
//        // Flip the values of the red, green, and blue components of the polygon's color.
//        int color = polygon.getStrokeColor() ^ 0x00ffffff;
//        polygon.setStrokeColor(color);
//        color = polygon.getFillColor() ^ 0x00ffffff;
//        polygon.setFillColor(color);
//
//        Toast.makeText(this, "Area type " + polygon.getTag().toString(), Toast.LENGTH_SHORT).show();
//    }

    private static final int COLOR_WHITE_ARGB = 0xffffffff;
    private static final int COLOR_DARK_GREEN_ARGB = 0xff388E3C;
    private static final int COLOR_LIGHT_GREEN_ARGB = 0xff81C784;
    private static final int COLOR_DARK_ORANGE_ARGB = 0xffF57F17;
    private static final int COLOR_LIGHT_ORANGE_ARGB = 0xffF9A825;

    private static final int POLYGON_STROKE_WIDTH_PX = 8;
    private static final int PATTERN_DASH_LENGTH_PX = 20;
    private static final PatternItem DASH = new Dash(PATTERN_DASH_LENGTH_PX);

    // Create a stroke pattern of a gap followed by a dash.
    private static final List<PatternItem> PATTERN_POLYGON_ALPHA = Arrays.asList(GAP, DASH);

    // Create a stroke pattern of a dot followed by a gap, a dash, and another gap.
    private static final List<PatternItem> PATTERN_POLYGON_BETA =
            Arrays.asList(DOT, GAP, DASH, GAP);

    /**
     * Styles the polygon, based on type.
     * @param polygon The polygon object that needs styling.
     */
//    private void stylePolygon(Polygon polygon) {
//        String type = "";
//        // Get the data object stored with the polygon.
//        if (polygon.getTag() != null) {
//            type = polygon.getTag().toString();
//        }
//
//        List<PatternItem> pattern = null;
//        int strokeColor = COLOR_BLACK_ARGB;
//        int fillColor = COLOR_WHITE_ARGB;
//
//        switch (type) {
//            // If no type is given, allow the API to use the default.
//            case "alpha":
//                // Apply a stroke pattern to render a dashed line, and define colors.
//                pattern = PATTERN_POLYGON_ALPHA;
//                strokeColor = COLOR_DARK_GREEN_ARGB;
//                fillColor = COLOR_LIGHT_GREEN_ARGB;
//                break;
//            case "beta":
//                // Apply a stroke pattern to render a line of dots and dashes, and define colors.
//                pattern = PATTERN_POLYGON_BETA;
//                strokeColor = COLOR_DARK_ORANGE_ARGB;
//                fillColor = COLOR_LIGHT_ORANGE_ARGB;
//                break;
//        }

//        polygon.setStrokePattern(pattern);
//        polygon.setStrokeWidth(POLYGON_STROKE_WIDTH_PX);
//        polygon.setStrokeColor(strokeColor);
//        polygon.setFillColor(fillColor);
    }





//    private String getUrl(LatLng origin, LatLng dest, String mode) {
//String url =""
//    }
