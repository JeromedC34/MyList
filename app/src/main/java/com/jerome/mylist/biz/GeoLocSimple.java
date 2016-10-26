// Example found on http://www.vogella.com/tutorials/AndroidLocationAPI/article.html
package com.jerome.mylist.biz;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

public class GeoLocSimple implements LocationListener {

    private Context context;
    private LocationManager locationManager;
    private String provider;
    private Location lastLocation;

    public GeoLocSimple(Context context) {
        this.context = context;
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        // Define the criteria how to select the location provider -> use default
        provider = locationManager.getBestProvider(new Criteria(), false);
        Location location = null;
        if (checkLocationPermission()) {
            location = locationManager.getLastKnownLocation(provider);
        }
        if (location != null) {
            Toast.makeText(context, "Provider (" + provider + ") has been selected.", Toast.LENGTH_LONG).show();
            onLocationChanged(location);
        }
    }

    public Location getLastLocation() {
        if (lastLocation != null) {
            return lastLocation;
        } else if (checkLocationPermission()) {
            lastLocation = locationManager.getLastKnownLocation(provider);
            return lastLocation;
        } else {
            return null;
        }
    }

    private boolean checkLocationPermission() {
        boolean result = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ((Activity) context).requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 3);
                Toast.makeText(context, "You should give permission for Geolocation!", Toast.LENGTH_LONG).show();
                result = false;
            }
        }
        return result;
    }

    /* Request updates at startup */
    public void onResume() {
        if (checkLocationPermission()) {
            locationManager.requestLocationUpdates(provider, 400, 1, this);
        }
    }

    /* Remove the locationlistener updates when Activity is paused */
    public void onPause() {
        if (checkLocationPermission()) {
            locationManager.removeUpdates(this);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        lastLocation = location;
        Toast.makeText(context, "GeoLocSimple.onLocationChanged: (" + location.getLatitude() + ", " + location.getLongitude() + ")", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(context, "Enabled new provider (" + provider + ")", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(context, "Disabled provider (" + provider + ")", Toast.LENGTH_SHORT).show();
    }
}