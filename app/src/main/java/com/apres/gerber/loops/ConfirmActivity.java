package com.apres.gerber.loops;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.*;
import android.view.MenuItem;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.text.DecimalFormat;

/**
 * Created by cedriclinares on 10/23/16.
 */

public class ConfirmActivity extends AppCompatActivity implements View.OnClickListener {
    private Button mSubmit;
    private Button mNext;
    private Button mPrev;

    private Marker m1;
    private Marker m2;
    private Marker m3;
    private Marker m4;
    private Marker m5;
    private Marker m6;
    private Marker m7;
    private Marker m8;

    public static TextView mTextview;
    private MenuItem mMenuItem;
    public static double miles;
    public static double kilometers;
    public static int meters;
    public static TextView mAltitude;
    public static TextView mDistance;
    public static double Altitude;
    static DecimalFormat df = new DecimalFormat("00.000");
    public int clicks = 0;


    public double lat = MapsActivity.lat;
    public double lng = MapsActivity.lng;
    public float changeInLat = MapsActivity.changeInLat;
    public float changeInLng = MapsActivity.changeInLng;
    public static GoogleMap mGoogleMap;
    public static float constant = (float) Math.sqrt(2) / 2;
    public static PolylineOptions rectLine;
    public static Polyline mPolyline;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.confirm);

        mMenuItem = (MenuItem) findViewById(android.R.id.home);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        super.onOptionsItemSelected(mMenuItem);

        //Intent prevIntent = new Intent(.getContext(),MapsActivity.class);
        //startActivityForResult(myIntent,0);

        mSubmit = (Button) findViewById(R.id.Submit);
        mSubmit.setOnClickListener(this);
        mNext = (Button) findViewById(R.id.next);
        mNext.setOnClickListener(this);
        mPrev = (Button) findViewById(R.id.prev);
        mPrev.setOnClickListener(this);
        mTextview = (TextView) findViewById(R.id.distance);
        mDistance = (TextView) findViewById(R.id.distance);
        mAltitude = (TextView) findViewById(R.id.altitude);

//        LatLng num = new LatLng(36.11, -122.01);
//        Marker m112 = new Marker({
//                .position(num)
//                .title("Testin")
//                .draggable(true)
//        });

        mGoogleMap.addMarker(m112);

        mGoogleMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {
                Toast.makeText(ConfirmActivity.this, marker.getId() + " is selected1.",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onMarkerDrag(Marker marker) {
                Toast.makeText(ConfirmActivity.this, marker.getId() + " is selected2.",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                Toast.makeText(ConfirmActivity.this, marker.getId() + " is selected3.",
                        Toast.LENGTH_SHORT).show();
            }
        });

        setMapSettings();
        calcLoop();

    }
//
//    @Override
//    public void onMarkerDragStart(Marker marker) {
//        LatLng position=marker.getPosition();
//
//    }
//
//    @Override
//    public void onMarkerDrag(Marker marker) {
//        LatLng position=marker.getPosition();
//
//    }
//
//    @Override
//    public void onMarkerDragEnd(Marker marker) {
//        LatLng position=marker.getPosition();
//
//    }


    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Submit:
                MapsActivity.location = MapsActivity.locationManager.getLastKnownLocation(MapsActivity.provider);
                Intent myIntent = new Intent(v.getContext(),ChronoActivity.class);
                startActivityForResult(myIntent,0);
                break;
            case R.id.next:
                mGoogleMap.clear();
                clicks++;
                switch (clicks % 4) {
                    case 0:
                        makeLoop(southLoop(lat, lng, changeInLat, changeInLng));
                        break;
                    case 1:
                        makeLoop(eastLoop(lat, lng, changeInLat, changeInLng));
                        break;
                    case 2:
                        makeLoop(northLoop(lat, lng, changeInLat, changeInLng));
                        break;
                    case 3:
                        makeLoop(westLoop(lat, lng, changeInLat, changeInLng));
                        break;
                }
                break;
            case R.id.prev:
                mGoogleMap.clear();
                clicks--;
                switch (clicks % 4) {
                    case 0:
                        makeLoop(southLoop(lat, lng, changeInLat, changeInLng));
                        break;
                    case 1:
                        makeLoop(eastLoop(lat, lng, changeInLat, changeInLng));
                        break;
                    case 2:
                        makeLoop(northLoop(lat, lng, changeInLat, changeInLng));
                        break;
                    case 3:
                        makeLoop(westLoop(lat, lng, changeInLat, changeInLng));
                        break;
                }
                break;
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Toast.makeText(this, "Home Button Pressed", Toast.LENGTH_LONG);
                Intent myIntent = new Intent(ConfirmActivity.this, MapsActivity.class);
                startActivityForResult(myIntent,0);
                return true;
            case R.id.option1:
                //TODO add what to do
                return true;

            case R.id.option2:
                //TODO add what to do
                return true;

            case R.id.option3:
                //TODO add what to do
                return true;
            default:
                return false;
        }
    }

    public void findDirections(ArrayList <LatLng> circle, String mode)
    {

        Map<String, String> map = new HashMap<String, String>();

        map.put(GetDirectionsAsyncTask.USER_CURRENT_LAT, String.valueOf(circle.get(0).latitude));
        map.put(GetDirectionsAsyncTask.USER_CURRENT_LONG, String.valueOf(circle.get(0).longitude));
        map.put(GetDirectionsAsyncTask.Waypoint1_Lat, String.valueOf(circle.get(1).latitude));
        map.put(GetDirectionsAsyncTask.Waypoint1_Long, String.valueOf(circle.get(1).longitude));
        map.put(GetDirectionsAsyncTask.Waypoint2_Lat, String.valueOf(circle.get(2).latitude));
        map.put(GetDirectionsAsyncTask.Waypoint2_Long, String.valueOf(circle.get(2).longitude));
        map.put(GetDirectionsAsyncTask.Waypoint3_Lat, String.valueOf(circle.get(3).latitude));
        map.put(GetDirectionsAsyncTask.Waypoint3_Long, String.valueOf(circle.get(3).longitude));
        map.put(GetDirectionsAsyncTask.Waypoint4_Lat, String.valueOf(circle.get(4).latitude));
        map.put(GetDirectionsAsyncTask.Waypoint4_Long, String.valueOf(circle.get(4).longitude));
        map.put(GetDirectionsAsyncTask.Waypoint5_Lat, String.valueOf(circle.get(5).latitude));
        map.put(GetDirectionsAsyncTask.Waypoint5_Long, String.valueOf(circle.get(5).longitude));
        map.put(GetDirectionsAsyncTask.Waypoint6_Lat, String.valueOf(circle.get(6).latitude));
        map.put(GetDirectionsAsyncTask.Waypoint6_Long, String.valueOf(circle.get(6).longitude));
        map.put(GetDirectionsAsyncTask.Waypoint7_Lat, String.valueOf(circle.get(7).latitude));
        map.put(GetDirectionsAsyncTask.Waypoint7_Long, String.valueOf(circle.get(7).longitude));
        map.put(GetDirectionsAsyncTask.DIRECTIONS_MODE, mode);

        GetDirectionsAsyncTask asyncTask = new GetDirectionsAsyncTask(this);
        asyncTask.execute(map);
    }

    public void makeLoop (ArrayList<LatLng> circle) {
        findDirections(circle, GMapV2Direction.MODE_WALKING);
        m1 = MapsActivity.addMarker(circle.get(0), 1);
        m2 = MapsActivity.addMarker(circle.get(1), 2);
        m3 = MapsActivity.addMarker(circle.get(2), 3);
        m4 = MapsActivity.addMarker(circle.get(3), 4);
        m5 = MapsActivity.addMarker(circle.get(4), 5);
        m6 = MapsActivity.addMarker(circle.get(5), 6);
        m7 = MapsActivity.addMarker(circle.get(6), 7);
        m8 = MapsActivity.addMarker(circle.get(7), 8);
        //mGoogleMap.setOnMarkerDragListener(this);
    }


    public void calcLoop(){
        clicks = 1028;
        lat = MapsActivity.location.getLatitude();
        lng = MapsActivity.location.getLongitude();

        //String input = MapsActivity.mEditDistance.getText().toString();
        String inputMiles = MapsActivity.mEditDistance.getText().toString();
        double inMiles = Double.parseDouble(inputMiles);
        double inKilometers = (.6214)*inMiles;
        String inputKilometers = String.valueOf(inKilometers);

        double circumference;

        if(MapsActivity.kiloIsLength) {
            circumference = Double.parseDouble(inputKilometers)-Double.parseDouble(inputKilometers)*.1;
        }
        else {
            circumference = Double.parseDouble(inputMiles) - Double.parseDouble(inputMiles) * .1;
        }


        //double circumference = Double.parseDouble(input)-Double.parseDouble(input)*.1;
        float distance = (float) (circumference / Math.PI);
        changeInLat = (float) Math.toDegrees(distance / MapsActivity.radOfEarth);
        changeInLng = (float) Math.toDegrees(distance / MapsActivity.radOfEarth);


        if (mPolyline != null) {
            mGoogleMap.clear();
        }

        makeLoop(southLoop(lat, lng, changeInLat, changeInLng));
    }

    public void handleGetDirectionsResult(ArrayList<LatLng> directionPoints) {

        rectLine = new PolylineOptions().width(10).color(Color.RED);

        for(int i = 0 ; i < directionPoints.size() ; i++)
        {
            rectLine.add(directionPoints.get(i));
        }

        mPolyline = this.mGoogleMap.addPolyline(rectLine);
        
        Altitude = GetDirectionsAsyncTask.altitude;
        mAltitude.setText("Altitude: " + df.format(Altitude) + " m");
        if(MapsActivity.kiloIsLength) {
            meters = GetDirectionsAsyncTask.distance;
            kilometers = (double) meters/1000;
            mTextview.setText("Distance: " + kilometers + " km");
        }
        else {
            meters = GetDirectionsAsyncTask.distance;
            miles = (double) meters/1600;
            mTextview.setText("Distance: " + miles + " mi");
        }

    }
    public static ArrayList<LatLng> southLoop (double lat, double lng, float changeInLat, float changeInLng){
        ArrayList<LatLng> circle = new ArrayList<LatLng>();

        LatLng point1 = new LatLng(lat, lng);
        LatLng point2 = new LatLng(lat - changeInLat*(1-constant)/2, lng - changeInLng*constant/2);
        LatLng point3 = new LatLng(lat - changeInLat/2, lng - changeInLng/2);
        LatLng point4 = new LatLng(lat - changeInLat/2-changeInLat*constant/2, lng - changeInLng*constant/2);
        LatLng point5 = new LatLng(lat - changeInLat, lng);
        LatLng point6 = new LatLng(lat - changeInLat/2-changeInLat*constant/2, lng + changeInLng*constant/2);
        LatLng point7 = new LatLng(lat - changeInLat/2, lng + changeInLng/2);
        LatLng point8 = new LatLng(lat - changeInLat*(1-constant)/2, lng + changeInLng*constant/2);

        circle.add(0, point1);
        circle.add(1, point2);
        circle.add(2, point3);
        circle.add(3, point4);
        circle.add(4, point5);
        circle.add(5, point6);
        circle.add(6, point7);
        circle.add(7, point8);
        return circle;
    }

    public static ArrayList<LatLng> northLoop (double lat, double lng, float changeInLat, float changeInLng){
        ArrayList<LatLng> circle = new ArrayList<LatLng>();

        LatLng point1 = new LatLng(lat, lng);
        LatLng point2 = new LatLng(lat + changeInLat*(1-constant)/2, lng - changeInLng*constant/2);
        LatLng point3 = new LatLng(lat + changeInLat/2, lng - changeInLng/2);
        LatLng point4 = new LatLng(lat + changeInLat/2 + changeInLat*constant/2, lng - changeInLng*constant/2);
        LatLng point5 = new LatLng(lat + changeInLat, lng);
        LatLng point6 = new LatLng(lat + changeInLat/2 + changeInLat*constant/2, lng + changeInLng*constant/2);
        LatLng point7 = new LatLng(lat + changeInLat/2, lng + changeInLng/2);
        LatLng point8 = new LatLng(lat + changeInLat*(1-constant)/2, lng + changeInLng*constant/2);

        circle.add(0, point1);
        circle.add(1, point2);
        circle.add(2, point3);
        circle.add(3, point4);
        circle.add(4, point5);
        circle.add(5, point6);
        circle.add(6, point7);
        circle.add(7, point8);

        return circle;
    }

    public static ArrayList<LatLng> eastLoop (double lat, double lng, float changeInLat, float changeInLng){
        ArrayList<LatLng> circle = new ArrayList<LatLng>();


        LatLng point1 = new LatLng(lat, lng);
        LatLng point2 = new LatLng(lat - changeInLat*constant/2,lng + changeInLng*(1-constant)/2);
        LatLng point3 = new LatLng(lat - changeInLat/2, lng + changeInLng/2);
        LatLng point4 = new LatLng(lat - changeInLat*constant/2, lng + changeInLng/2 + changeInLng*constant/2);
        LatLng point5 = new LatLng(lat,lng + changeInLng);
        LatLng point6 = new LatLng(lat + changeInLat*constant/2, lng + changeInLng/2 + changeInLng*constant/2);
        LatLng point7 = new LatLng(lat + changeInLat/2, lng + changeInLng/2);
        LatLng point8 = new LatLng(lat + changeInLat*constant/2, lng + changeInLng*(1-constant)/2);

        circle.add(0, point1);
        circle.add(1, point2);
        circle.add(2, point3);
        circle.add(3, point4);
        circle.add(4, point5);
        circle.add(5, point6);
        circle.add(6, point7);
        circle.add(7, point8);

        return circle;
    }

    public static ArrayList<LatLng> westLoop (double lat, double lng, float changeInLat, float changeInLng){
        ArrayList<LatLng> circle = new ArrayList<LatLng>();

        LatLng point1 = new LatLng(lat, lng);
        LatLng point2 = new LatLng(lat - changeInLat*constant/2,lng - changeInLng*(1-constant)/2);
        LatLng point3 = new LatLng(lat - changeInLat/2, lng - changeInLng/2);
        LatLng point4 = new LatLng(lat - changeInLat*constant/2, lng - changeInLng/2 - changeInLng*constant/2);
        LatLng point5 = new LatLng(lat,lng - changeInLng);
        LatLng point6 = new LatLng(lat + changeInLat*constant/2, lng - changeInLng/2 - changeInLng*constant/2);
        LatLng point7 = new LatLng(lat + changeInLat/2, lng - changeInLng/2);
        LatLng point8 = new LatLng(lat + changeInLat*constant/2, lng - changeInLng*(1-constant)/2);


        circle.add(0, point1);
        circle.add(1, point2);
        circle.add(2, point3);
        circle.add(3, point4);
        circle.add(4, point5);
        circle.add(5, point6);
        circle.add(6, point7);
        circle.add(7, point8);

        return circle;
    }

    public void setMapSettings(){
        MapsActivity.mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        ConfirmActivity.mGoogleMap = MapsActivity.mapFragment.getMap();
        ConfirmActivity.mGoogleMap.getUiSettings().setZoomControlsEnabled(true);

        try {
            LatLng camera = new LatLng(MapsActivity.location.getLatitude(), MapsActivity.location.getLongitude());
            MapsActivity.mCameraPosition = new CameraPosition.Builder().target(camera)
                    .zoom(15.5f)
                    .bearing(0)
                    .tilt(25)
                    .build();
            ConfirmActivity.mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(MapsActivity.mCameraPosition));
            MapsActivity.addMarker(camera, 1);
        } catch (NullPointerException e) {
            Toast.makeText(this, "No Location Found", Toast.LENGTH_LONG).show();
        }

    }

}
