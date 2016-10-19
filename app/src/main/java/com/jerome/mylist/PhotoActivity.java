package com.jerome.mylist;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class PhotoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PhotoFragment photo = new PhotoFragment();
        photo.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().add(R.id.activity_photo, photo).commit();
        setContentView(R.layout.activity_photo);

    }
}
