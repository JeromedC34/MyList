package com.jerome.mylist.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.jerome.mylist.R;

public class MapsActivity extends FragmentActivity implements LocationListener, View.OnClickListener,
        OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks,
        ActivityCompat.OnRequestPermissionsResultCallback {

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private boolean mRequestingLocationUpdates = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateValuesFromBundle(savedInstanceState);

        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        Button buttonGeoLoc = (Button) findViewById(R.id.btn_geoloc);
        buttonGeoLoc.setOnClickListener(this);
    }

    private void updateValuesFromBundle(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
//            // Update the value of mRequestingLocationUpdates from the Bundle, and
//            // make sure that the Start Updates and Stop Updates buttons are
//            // correctly enabled or disabled.
//            if (savedInstanceState.keySet().contains(REQUESTING_LOCATION_UPDATES_KEY)) {
//                mRequestingLocationUpdates = savedInstanceState.getBoolean(
//                        REQUESTING_LOCATION_UPDATES_KEY);
//                setButtonsEnabledState();
//            }
//
//            // Update the value of mCurrentLocation from the Bundle and update the
//            // UI to show the correct latitude and longitude.
//            if (savedInstanceState.keySet().contains(LOCATION_KEY)) {
//                // Since LOCATION_KEY was found in the Bundle, we can be sure that
//                // mCurrentLocationis not null.
//                mCurrentLocation = savedInstanceState.getParcelable(LOCATION_KEY);
//            }
//
//            // Update the value of mLastUpdateTime from the Bundle and update the UI.
//            if (savedInstanceState.keySet().contains(LAST_UPDATED_TIME_STRING_KEY)) {
//                mLastUpdateTime = savedInstanceState.getString(
//                        LAST_UPDATED_TIME_STRING_KEY);
//            }
//            updateUI();
        }
    }

    @Override
    public void onClick(View v) {
        setMyLocation();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        Toast.makeText(this, "onLocationChanged: " + mLastLocation.getLatitude() + ", " + mLastLocation.getLongitude(), Toast.LENGTH_LONG).show();
        setLatLng(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()), "Here!!");
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

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mGoogleApiClient.isConnected()) {
            startLocationUpdates();
        }
    }

    protected void startLocationUpdates() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

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
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
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
    }

    public void setMyLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 3);
                Toast.makeText(this, "You should give permission for Geolocation", Toast.LENGTH_LONG).show();
                return;
            }
        }
        mMap.setMyLocationEnabled(true);
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            setLatLng(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()), "Here!");
        }
    }

    // Add a marker and move the camera
    public void setLatLng(LatLng newLatLng, String string) {
        mMap.addMarker(new MarkerOptions().position(newLatLng).title(string));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(newLatLng));
    }

    public void onSaveInstanceState(Bundle savedInstanceState) {
//        savedInstanceState.putBoolean(REQUESTING_LOCATION_UPDATES_KEY,
//                mRequestingLocationUpdates);
//        savedInstanceState.putParcelable(LOCATION_KEY, mCurrentLocation);
//        savedInstanceState.putString(LAST_UPDATED_TIME_STRING_KEY, mLastUpdateTime);
        super.onSaveInstanceState(savedInstanceState);
    }
}
