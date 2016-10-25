package com.apres.gerber.loops;

import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.*;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout mconfirmation;
    private LinearLayout mfindLoops;
    public static EditText mEditDistance;
    private Button mButton;

    public static String provider;
    public static LocationManager locationManager;
    public static Location location;
    public static LocationListener mLocationListener;
    public static CameraPosition mCameraPosition;
    public static SupportMapFragment mapFragment;

    public static Marker mMarker;
    public static MenuItem mMenuItem;
    private TextView mTextview;

    public static final float radOfEarth = 3950;
    public static double lat;
    public static double lng;
    public static float changeInLat;
    public static float changeInLng;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!isGooglePlayServicesAvailable()) {
            finish();
        }
        setContentView(R.layout.activity_maps);

        mMenuItem = (MenuItem) findViewById(android.R.id.home);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        super.onOptionsItemSelected(mMenuItem);

        mconfirmation = (LinearLayout) findViewById(R.id.confirmaton);
        mfindLoops = (LinearLayout) findViewById(R.id.findLoops);
        mEditDistance = (EditText) findViewById(R.id.edt_text);
        mButton = (Button) findViewById(R.id.loop_button);
        mTextview = (TextView) findViewById(R.id.distance);

        setMapSettings();

        mEditDistance.setOnClickListener(this);
        mButton.setOnClickListener(this);



    }

    public void onClick(View v) {
        switch(v.getId()){
            case R.id.loop_button:
                location = locationManager.getLastKnownLocation(provider);

                try {
                    Double.parseDouble(mEditDistance.getText().toString());
                    // Initialize the location fields
                    if (location != null) {

                        Intent myIntent = new Intent(v.getContext(),ConfirmActivity.class);
                        startActivityForResult(myIntent,0);
                    }
                    else
                        Toast.makeText(MapsActivity.this, "Cannot Find Location", Toast.LENGTH_LONG).show();
                }
                catch (NumberFormatException e) {
                    Log.i("Check EditText","Input is not a number");
                    Toast.makeText(MapsActivity.this, "Must input a number", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.edt_text:
                mEditDistance.getText().clear();
                break;
        }
    }

    public void setMapSettings(){
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        ConfirmActivity.mGoogleMap = mapFragment.getMap();
        ConfirmActivity.mGoogleMap.getUiSettings().setZoomControlsEnabled(true);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mLocationListener = new MyLocationListener();
        // Define the criteria how to select the location in provider -> use
        // default
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mLocationListener);
        location = locationManager.getLastKnownLocation(provider);

        try {
            LatLng camera = new LatLng(location.getLatitude(), location.getLongitude());
            mCameraPosition = new CameraPosition.Builder().target(camera)
                    .zoom(15.5f)
                    .bearing(0)
                    .tilt(25)
                    .build();
            ConfirmActivity.mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(mCameraPosition));
            addMarker(camera, 1);
        } catch (NullPointerException e) {
            Toast.makeText(this, "No Location Found", Toast.LENGTH_LONG).show();
        }
    }

    private LatLngBounds createLatLngBoundsObject(LatLng firstLocation, LatLng secondLocation)
    {
        if (firstLocation != null && secondLocation != null)
        {
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            builder.include(firstLocation).include(secondLocation);

            return builder.build();
        }
        return null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Toast.makeText(this, "Home Button Pressed", Toast.LENGTH_LONG);
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

    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, this, 0).show();
            return false;
        }
    }

    public static void addMarker(LatLng point, int order){
        mMarker = ConfirmActivity.mGoogleMap.addMarker(new MarkerOptions()
                .position(point)
                .title("This is point" + order)
                .snippet("Lat: " + point.latitude + " Lng: " + point.longitude)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher)));
    }

    public final class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            LatLng curLocation = new LatLng(location.getLatitude(),location.getLongitude());
            if (mMarker!=null) {
                mMarker.remove();
                addMarker(curLocation, 10);
            }
            else
                addMarker(curLocation, 10);
        }

        @Override
        public void onProviderDisabled(String provider) {
            // called when the GPS provider is turned off (user turning off the GPS on the phone)
        }

        @Override
        public void onProviderEnabled(String provider) {
            // called when the GPS provider is turned on (user turning on the GPS on the phone)
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            // called when the status of the GPS provider changes
        }
    }




}