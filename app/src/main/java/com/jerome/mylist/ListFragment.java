package com.jerome.mylist;

import android.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

public class ListFragment extends Fragment implements View.OnClickListener, OnResponseListener {
    public EditText editText;
    MyListAdapter myListAdapter;
    boolean bound = false;
    private MainActivity mainActivity;
    private BoundService boundService;
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            BoundService.ServiceBinder binder = (BoundService.ServiceBinder) service;
            boundService = binder.getService();
            bound = true;
            boundService.setOnResponseListener(ListFragment.this);
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            bound = false;
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        ListView listView = (ListView) view.findViewById(R.id.list);
        myListAdapter = new MyListAdapter(getActivity());
        listView.setAdapter(myListAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), "Test - " + myListAdapter.getItem(position).getUrl() + " - " + position + " - " + id, Toast.LENGTH_SHORT).show();
                View detailsFrame = getActivity().findViewById(R.id.fragmentPhoto);
                // both fragments displayed
                if (detailsFrame != null && detailsFrame.getVisibility() == View.VISIBLE) {
                } else {
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), PhotoActivity.class);
                    intent.putExtra("title", myListAdapter.getItem(position).getTitle());
                    intent.putExtra("url", myListAdapter.getItem(position).getUrl());
                    startActivity(intent);
                }
            }
        });
        editText = (EditText) view.findViewById(R.id.inp_search);

        Button changeButton = (Button) view.findViewById(R.id.btn_change);
        changeButton.setOnClickListener(this);
        return view;
    }

    public void onClick(View view) {
        if (bound) {
            boundService.getPhotos(editText.getText().toString());
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.not_binded), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Intent intent = new Intent(getActivity(), BoundService.class);
        getActivity().bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (bound) {
            getActivity().unbindService(connection);
            bound = false;
        }
    }

    @Override
    public void onResponse(List<FlickrPhoto> list) {
        myListAdapter.setList(list);
    }
}
