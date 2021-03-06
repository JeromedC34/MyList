package com.jerome.mylist.ui;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ContentViewEvent;
import com.google.android.gms.maps.model.LatLng;
import com.jerome.mylist.R;
import com.jerome.mylist.biz.BoundService;
import com.jerome.mylist.biz.GeoLoc;
import com.jerome.mylist.biz.MyPhotos;
import com.jerome.mylist.biz.OnGeoLocListener;
import com.jerome.mylist.biz.OnResponseListener;
import com.jerome.mylist.dat.FlickrPhoto;

import org.honorato.multistatetogglebutton.MultiStateToggleButton;
import org.honorato.multistatetogglebutton.ToggleButton;

import java.util.List;

public class ListFragment extends Fragment implements View.OnClickListener, OnResponseListener, OnGeoLocListener {
    private static MyListAdapter myListAdapter;
    private static MyListAdapter myHistoryAdapter;
    private static LinearLayout searchBlock;
    private static ListView historyView;
    private MultiStateToggleButton mstButton;
    private EditText editText;
    private ItemClicked mCallback;
    private MyPhotos myPhotos;
    private boolean bound = false;
    private BoundService boundService;
    private GeoLoc geoLoc;
    private LatLng latLng;

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
    public void onAttach(Context activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (ItemClicked) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement ItemClicked");
        }
    }

    @Override
    public void onDetach() {
        mCallback = null; // => avoid leaking, thanks @Deepscorn
        super.onDetach();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        geoLoc = new GeoLoc(this.getContext());
        geoLoc.setOnGeoLocListener(this);
        geoLoc.onCreate();
        latLng = geoLoc.getLastLatLng();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        ListView listView = (ListView) view.findViewById(R.id.list);
        searchBlock = (LinearLayout) view.findViewById(R.id.search_block);
        historyView = (ListView) view.findViewById(R.id.history);
        myListAdapter = new MyListAdapter(getActivity());
        myHistoryAdapter = new MyListAdapter(getActivity());
        myPhotos = new MyPhotos(getActivity());
        listView.setAdapter(myListAdapter);
        historyView.setAdapter(myHistoryAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                View detailsFrame = getActivity().findViewById(R.id.fragmentPhoto);
                FlickrPhoto myPhoto;
                myPhoto = myListAdapter.getItem(position);
                myPhoto = myPhotos.setLatLng(myPhoto, geoLoc.getLastLatLng());
                myPhoto = myPhotos.seen(myPhoto);
//                myHistoryAdapter.addItem(myPhoto);
                // both fragments displayed
                if (detailsFrame != null && detailsFrame.getVisibility() == View.VISIBLE) {
                    mCallback.sendItem(myPhoto);
                } else {
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), PhotoActivity.class);
                    intent.putExtra("photo", myPhoto);
                    startActivity(intent);
                }
            }
        });
        historyView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                View detailsFrame = getActivity().findViewById(R.id.fragmentPhoto);
                FlickrPhoto myPhoto = myHistoryAdapter.getItem(position);
                myPhoto = myPhotos.seen(myPhoto);
                // both fragments displayed
                if (detailsFrame != null && detailsFrame.getVisibility() == View.VISIBLE) {
                    mCallback.sendItem(myPhoto);
                } else {
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), PhotoActivity.class);
                    intent.putExtra("photo", myPhoto);
                    startActivity(intent);
                }
            }
        });
        editText = (EditText) view.findViewById(R.id.inp_search);

        Button searchButton = (Button) view.findViewById(R.id.btn_search);
        searchButton.setOnClickListener(this);

        return view;
    }

    private void initLists() {
        mstButton.setValue(0);
        historyView.setVisibility(View.GONE);
        searchBlock.setVisibility(View.VISIBLE);
    }

    public void onClick(View view) {
        if (bound) {
            if (!"".equals(editText.getText().toString())) {
                initLists();
                boundService.getPhotos(editText.getText().toString());
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.not_binded), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        geoLoc.onStart();
        Intent intent = new Intent(getActivity(), BoundService.class);
        getActivity().bindService(intent, connection, Context.BIND_AUTO_CREATE);
        if (mstButton == null) {
            mstButton = (MultiStateToggleButton) getActivity().findViewById(R.id.mstb_multi_id);
            initLists();
        }
        mstButton.setOnValueChangedListener(new ToggleButton.OnValueChangedListener() {
            @Override
            public void onValueChanged(int position) {
                // TODO avoid to change visibilities -> use fragments
                if (position == 0) {
                    historyView.setVisibility(View.GONE);
                    searchBlock.setVisibility(View.VISIBLE);
                    // TODO: Use your own attributes to track content views in your app
                    Answers.getInstance().logContentView(new ContentViewEvent()
                            .putContentName("MTBS")
                            .putContentType("Search")
                            .putContentId("1")
                            .putCustomAttribute("MyAttribute", "MyValue1"));
                } else if (position == 1) {
                    myHistoryAdapter.setList(myPhotos.getHistory());
                    searchBlock.setVisibility(View.GONE);
                    historyView.setVisibility(View.VISIBLE);
                    // TODO: Use your own attributes to track content views in your app
                    Answers.getInstance().logContentView(new ContentViewEvent()
                            .putContentName("MTBS")
                            .putContentType("History")
                            .putContentId("2")
                            .putCustomAttribute("MyAttribute", "MyValue2"));
                } else if (position == 2) {
                    historyView.setVisibility(View.GONE);
                    searchBlock.setVisibility(View.GONE);
                    // TODO: Use your own attributes to track content views in your app
                    Answers.getInstance().logContentView(new ContentViewEvent()
                            .putContentName("MTBS")
                            .putContentType("Favorites")
                            .putContentId("3")
                            .putCustomAttribute("MyAttribute", "MyValue3"));
                } else {
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), MapsActivity.class);
                    startActivity(intent);
                    mstButton.setValue(0);
                    // TODO: Use your own attributes to track content views in your app
                    Answers.getInstance().logContentView(new ContentViewEvent()
                            .putContentName("MTBS")
                            .putContentType("Map")
                            .putContentId("4")
                            .putCustomAttribute("MyAttribute", "MyValue4"));
                }
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        geoLoc.onStop();
        if (bound) {
            getActivity().unbindService(connection);
            bound = false;
        }
    }

    @Override
    public void onResponse(List<FlickrPhoto> list) {
        myListAdapter.setList(list);
        if (list.size() == 0) {
            Toast.makeText(getActivity(), R.string.no_result, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onFailure() {
        Toast.makeText(getActivity(), R.string.request_failure, Toast.LENGTH_LONG).show();
    }

    public void onGeoLocUpdate(LatLng newLatLng, String string) {
        latLng = newLatLng;
    }

    @Override
    public void onPause() {
        super.onPause();
        geoLoc.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        geoLoc.onResume();
    }

    public interface ItemClicked {
        void sendItem(FlickrPhoto photo);
    }
}
