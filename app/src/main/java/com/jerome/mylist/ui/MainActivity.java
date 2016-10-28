package com.jerome.mylist.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.jerome.mylist.R;
import com.jerome.mylist.biz.GeoLocSimple;
import com.jerome.mylist.dat.FlickrPhoto;

import hugo.weaving.DebugLog;
import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity implements ListFragment.ItemClicked, View.OnClickListener {
    GeoLocSimple geoLocSimple;

    @Override
    @DebugLog
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);
        geoLocSimple = new GeoLocSimple(this);
        Button button = (Button) findViewById(R.id.test_answers);
        button.setOnClickListener(this);
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

    @Override
    public void onClick(View v) {
        // TODO: Use your own string attributes to track common values over time
        // TODO: Use your own number attributes to track median value over time
        Answers.getInstance().logCustom(new CustomEvent("Video Played")
                .putCustomAttribute("Category", "Comedy")
                .putCustomAttribute("Length", 350));
    }
}
