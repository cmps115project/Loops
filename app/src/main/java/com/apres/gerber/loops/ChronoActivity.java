package com.apres.gerber.loops;

import android.app.Activity;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.fitness.ConfigApi;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import static com.apres.gerber.loops.R.layout.chrono;

/**
 * Created by cedriclinares on 10/23/16.
 */

public class ChronoActivity extends AppCompatActivity implements View.OnClickListener{

    Button startButton;
    Button stopButton;
    Button resetButton;
    Chronometer mchrono = new Chronometer(this);
    private TextView mTextView;
    long milli = SystemClock.elapsedRealtime()-mchrono.getBase();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(chrono);

        MapsActivity.mMenuItem = (MenuItem) findViewById(android.R.id.home);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        super.onOptionsItemSelected(MapsActivity.mMenuItem);

        startButton = (Button) findViewById(R.id.start);
        startButton.setOnClickListener(this);
        stopButton = (Button) findViewById(R.id.stop);
        stopButton.setOnClickListener(this);
        resetButton = (Button) findViewById(R.id.reset);
        resetButton.setOnClickListener(this);
        mchrono = (Chronometer) findViewById(R.id.chronometer);
        mTextView = (TextView) findViewById(R.id.distance2);

        mTextView.setText("Distance: " + ConfirmActivity.miles + " mi");

        setMapSettings();
        ConfirmActivity.mPolyline = ConfirmActivity.mGoogleMap.addPolyline(ConfirmActivity.rectLine);

    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start:
                mchrono.setBase(SystemClock.elapsedRealtime());
                mchrono.start();
                break;
            case R.id.stop:
                mchrono.setBase(SystemClock.elapsedRealtime());
                mchrono.stop();
                break;
            case R.id.reset:
                mchrono.setBase(0);
        }
    }

    public void setMapSettings(){
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        ConfirmActivity.mGoogleMap = mapFragment.getMap();
        ConfirmActivity.mGoogleMap.getUiSettings().setZoomControlsEnabled(true);

        MapsActivity.locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        MapsActivity.mLocationListener = new ChronoActivity.MyLocationListener();
        // Define the criteria how to select the location in provider -> use
        // default
        Criteria criteria = new Criteria();
        MapsActivity.provider = MapsActivity.locationManager.getBestProvider(criteria, false);
        MapsActivity.locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, MapsActivity.mLocationListener);
        MapsActivity.location = MapsActivity.locationManager.getLastKnownLocation(MapsActivity.provider);

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

    public final class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            LatLng curLocation = new LatLng(location.getLatitude(),location.getLongitude());
            if (MapsActivity.mMarker!=null) {
                MapsActivity.mMarker.remove();
                MapsActivity.addMarker(curLocation, 10);
            }
            else
                MapsActivity. addMarker(curLocation, 10);
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
