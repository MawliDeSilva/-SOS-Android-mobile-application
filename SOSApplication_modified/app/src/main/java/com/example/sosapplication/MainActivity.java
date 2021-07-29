package com.example.sosapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Date;


public class MainActivity extends AppCompatActivity implements LocationListener {

    private GoogleMap mMap;
    private LocationListener locationListener;
    private LocationManager locationManager;
    private final long MIN_TIME = 1000; //minimum time interval between location updates, in milliseconds
    private final long MIN_DISTANCE = 5; //minimum distance between location updates, in meters
    private LatLng latLang;
    private double latitude;
    private double longitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.INTERNET}, PackageManager.PERMISSION_GRANTED);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000*60, 100, this);

    }

    public void sendSMS(View view) {
        String mobileNo = "0716332197";
//        String mobileNo = "0719617436";
        long startTime = System.currentTimeMillis();
        while((System.currentTimeMillis() - startTime) < 3*60*1000){
            @SuppressLint("MissingPermission") Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            longitude = lastKnownLocation.getLongitude();
            latitude = lastKnownLocation.getLatitude();
            try {
                SmsManager smgr = SmsManager.getDefault();
                String location ="http://maps.google.com/?q="+Double.toString(latitude)+","+Double.toString(longitude);
//            smgr.sendTextMessage(mobileNo, null, Double.toString(latitude), null, null);
                smgr.sendTextMessage(mobileNo, null, "I'm Mawli (IM/2017/015)", null, null);
                smgr.sendTextMessage(mobileNo, null, "I'm in " +location, null, null);
                Toast.makeText(getApplicationContext(), "SMS Sent to " + mobileNo, Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "SMS Sending failed", Toast.LENGTH_LONG).show();
            }

            try {
                Thread.sleep(1000*60);
            } catch (InterruptedException e) {
                Toast.makeText(getApplicationContext(), "Interrupted", Toast.LENGTH_LONG).show();
            }
        }

    }

    @SuppressLint("MissingPermission")
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        locationListener=new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                try {
                    latLang = new LatLng(location.getLatitude(), location.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(latLang).title("Latitude :" + latLang.latitude + " & Longtitude :" + latLang.longitude));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLang));
                } catch (SecurityException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onStatusChanged (String provider, int status, Bundle extras) { }

            @Override
            public void onProviderEnabled (String provider) {}

            @Override
            public void onProviderDisabled (String provider) {}


        };

        try{
            locationManager=(LocationManager)getSystemService(LOCATION_SERVICE);
            //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,MIN_TIME,MIN_DISTANCE,locationListener);
            locationManager.requestLocationUpdates( LocationManager.GPS_PROVIDER,MIN_TIME,MIN_DISTANCE,locationListener);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
    }
}
