package com.jerome.mylist.biz;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

public class GeoLoc implements LocationListener, GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks, ActivityCompat.OnRequestPermissionsResultCallback {

    private GoogleApiClient mGoogleApiClient;
    private Location lastLocation;
    private Context context;
    private OnGeoLocListener onGeoLocListener;

    public void setOnGeoLocListener(OnGeoLocListener onGeoLocListener) {
        this.onGeoLocListener = onGeoLocListener;
    }
    public GeoLoc(Context context) {
        this.context = context;
    }

    public void onCreate() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(context)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        setMyLocation();
    }

    public LatLng getLastLatLng() {
        if (lastLocation != null) {
            return new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
        } else {
            return null;
        }
    }

    protected void startLocationUpdates() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if (checkLocationPermission()) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    public boolean checkLocationPermission() {
        boolean result = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ((Activity) context).requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 3);
                Toast.makeText(context, "You should give permission for Geolocation", Toast.LENGTH_LONG).show();
                result = false;
            }
        }
        return result;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ((Activity) context).onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void setMyLocation() {
        if (checkLocationPermission()) {
            setLastLocation(LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient));
        }
    }

    public void setLastLocation(Location lastLocation) {
        this.lastLocation = lastLocation;
        onGeoLocListener.onGeoLocUpdate(getLastLatLng(), "setLastLocation");
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

     @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        setLastLocation(location);
        Toast.makeText(context, "GeoLoc.onLocationChanged: " + lastLocation.getLatitude() + ", " + lastLocation.getLongitude(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e("GM_connection_failed", connectionResult.toString());
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        setMyLocation();
        startLocationUpdates();
    }

    public void onPause() {
        stopLocationUpdates();
    }

    public void onStart() {
        mGoogleApiClient.connect();
    }

    public void onStop() {
        mGoogleApiClient.disconnect();
    }

    public void onResume() {
        if (mGoogleApiClient.isConnected()) {
            startLocationUpdates();
        }
    }
}
