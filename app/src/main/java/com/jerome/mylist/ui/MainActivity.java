package com.jerome.mylist.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.jerome.mylist.R;
import com.jerome.mylist.biz.GeoLocSimple;
import com.jerome.mylist.dat.FlickrPhoto;

public class MainActivity extends AppCompatActivity implements ListFragment.ItemClicked {
    GeoLocSimple geoLocSimple;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        geoLocSimple = new GeoLocSimple(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        geoLocSimple.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        geoLocSimple.onPause();
    }

    @Override
    public void sendItem(FlickrPhoto photo) {
        // Get Fragment B
        PhotoFragment frag = (PhotoFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragmentPhoto);
        frag.setPhoto(findViewById(android.R.id.content), photo);
    }
}
