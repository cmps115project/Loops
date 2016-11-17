package com.apres.gerber.loops;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

import org.w3c.dom.Text;

import static com.apres.gerber.loops.R.layout.chrono;

/**
 * Created by cedriclinares on 10/23/16.
 */

public class ChronoActivity extends AppCompatActivity implements View.OnClickListener{

    LocationManager locationManager;
    LocationListener mLocationListener;
    Location location;
    String provider;

    Button startButton;
    Button stopButton;
    Button resetButton;
    Chronometer mchrono;
    boolean startPress=false;
    boolean stopPress=false;
    long time =0;
    private TextView mDistance;
    private TextView mAltitude;

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
        mDistance = (TextView) findViewById(R.id.distance2);
        mAltitude = (TextView) findViewById(R.id.altitude2);

        locationManager = MapsActivity.locationManager;
        provider = MapsActivity.provider;
        location = MapsActivity.location;

        mDistance.setText("Distance: " + ConfirmActivity.miles + " mi");
        mAltitude.setText("Altitude: " + ConfirmActivity.df.format(ConfirmActivity.Altitude) + " m");
        setMapSettings();
        ConfirmActivity.mPolyline = ConfirmActivity.mGoogleMap.addPolyline(ConfirmActivity.rectLine);

    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start:
                if(!startPress) {
                    mchrono.setBase(SystemClock.elapsedRealtime() + time);
                    mchrono.start();
                    startPress=true;
                    stopPress=false;
                }
                break;
            case R.id.stop:
                if (!stopPress) {
                    time = mchrono.getBase() - SystemClock.elapsedRealtime();
                    mchrono.stop();
                    startPress = false;
                    stopPress = true;
                }
                break;
            case R.id.reset:
                mchrono.setBase(SystemClock.elapsedRealtime());
                mchrono.stop();
                break;
        }
    }

    public void setMapSettings() {

        MapsActivity.mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        ConfirmActivity.mGoogleMap = MapsActivity.mapFragment.getMap();
        ConfirmActivity.mGoogleMap.getUiSettings().setZoomControlsEnabled(true);

        mLocationListener = new ChronoActivity.MyLocationListener();

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mLocationListener);
        location = locationManager.getLastKnownLocation(provider);

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
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Toast.makeText(this, "Home Button Pressed", Toast.LENGTH_LONG);
                Intent myIntent = new Intent(ChronoActivity.this, ConfirmActivity.class);
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

    public final class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            LatLng curLocation = new LatLng(location.getLatitude(),location.getLongitude());
            if (MapsActivity.mMarker!=null) {
                MapsActivity.mMarker.remove();
                MapsActivity.addMarker(curLocation, 10);
            }
            else
                MapsActivity.addMarker(curLocation, 10);
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
