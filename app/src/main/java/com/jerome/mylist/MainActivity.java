package com.jerome.mylist;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, OnResponseListener {
    public EditText editText;
    MyListAdapter myListAdapter;
    boolean bound = false;
    private BoundService boundService;
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            BoundService.ServiceBinder binder = (BoundService.ServiceBinder) service;
            boundService = binder.getService();
            bound = true;
            boundService.setListener(MainActivity.this);
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            bound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = (ListView) findViewById(R.id.list);
        myListAdapter = new MyListAdapter(this.getApplicationContext());
        listView.setAdapter(myListAdapter);
        editText = (EditText) findViewById(R.id.inp_search);

        Button changeButton = (Button) findViewById(R.id.btn_change);
        changeButton.setOnClickListener(this);
    }
    public void onClick(View view) {
//        List<String> myArrayList = new ArrayList<>();
//        for (int i = 0; i < 100; i++) {
//            myArrayList.add("Line " + (i + 1) + " - " + Math.random());
//        }
//        myListAdapter.setList(myArrayList);
        if (bound) {
            boundService.getPhotos(editText.getText().toString());
        } else {
            Toast.makeText(this, getResources().getString(R.string.not_binded), Toast.LENGTH_SHORT).show();
        }
    }

    public void setPhotosList(List<FlickrPhoto> myList) {
        myListAdapter.setList(myList);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, BoundService.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (bound) {
            unbindService(connection);
            bound = false;
        }
    }

    @Override
    public void onResponse(List<FlickrPhoto> list) {
        myListAdapter.setList(list);
    }
}
