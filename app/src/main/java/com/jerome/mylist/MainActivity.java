package com.jerome.mylist;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements ListFragment.ItemClicked {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    @Override
    public void sendItem(FlickrPhoto photo){
        // Get Fragment B
        PhotoFragment frag = (PhotoFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragmentPhoto);
        frag.setPhoto(findViewById(android.R.id.content), photo);
    }}
