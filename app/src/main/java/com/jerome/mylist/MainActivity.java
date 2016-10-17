package com.jerome.mylist;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    MyListAdapter myListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = (ListView) findViewById(R.id.list);
        myListAdapter = new MyListAdapter(this.getApplicationContext());
        listView.setAdapter(myListAdapter);

        Button changeButton = (Button) findViewById(R.id.btn_change);
        changeButton.setOnClickListener(this);
    }

    public void onClick(View view) {
        List<String> myArrayList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            myArrayList.add("Line " + (i + 1) + " - " + Math.random());
        }
        myListAdapter.setList(myArrayList);
    }
}
