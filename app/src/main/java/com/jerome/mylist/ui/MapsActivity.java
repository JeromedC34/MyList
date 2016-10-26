package com.jerome.mylist.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.jerome.mylist.R;
import com.jerome.mylist.biz.GeoLoc;
import com.jerome.mylist.biz.MyPhotos;
import com.jerome.mylist.biz.OnGeoLocListener;
import com.jerome.mylist.dat.FlickrPhoto;

import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, OnGeoLocListener {

    private GoogleMap mMap;
    private GeoLoc geoLoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        geoLoc = new GeoLoc(this);
        geoLoc.setOnGeoLocListener(this);
        geoLoc.onCreate();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (geoLoc.checkLocationPermission()) {
            mMap.setMyLocationEnabled(true);
            geoLoc.setMyLocation();
        }
        MyPhotos myPhotos = new MyPhotos(this);
        List<FlickrPhoto> favorites = myPhotos.getFavorites();
        for (FlickrPhoto photo : favorites) {
            onGeoLocUpdate(new LatLng(photo.getLat(), photo.getLon()), photo.getTitle());
        }
    }

    // Add a marker and move the camera
    public void onGeoLocUpdate(LatLng newLatLng, String string) {
        if (newLatLng != null) {
            mMap.addMarker(new MarkerOptions()
                    .position(newLatLng)
                    .title(string)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
            );
            mMap.moveCamera(CameraUpdateFactory.newLatLng(newLatLng));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        geoLoc.onPause();
    }

    @Override
    protected void onStart() {
        geoLoc.onStart();
        super.onStart();
    }

    @Override
    protected void onStop() {
        geoLoc.onStop();
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        geoLoc.onResume();
    }
}
