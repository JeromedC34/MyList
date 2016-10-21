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

import org.honorato.multistatetogglebutton.MultiStateToggleButton;
import org.honorato.multistatetogglebutton.ToggleButton;

import java.util.List;

public class ListFragment extends Fragment implements View.OnClickListener, OnResponseListener {
    private static MyListAdapter myListAdapter;
    private static MyListAdapter myHistoryAdapter;
    private static MultiStateToggleButton mstButton;
    private EditText editText;
    private ItemClicked mCallback;
    private ListView listView;
    private ListView historyView;
    private boolean bound = false;
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        listView = (ListView) view.findViewById(R.id.list);
        historyView = (ListView) view.findViewById(R.id.history);
        myListAdapter = new MyListAdapter(getActivity());
        myHistoryAdapter = new MyListAdapter(getActivity());
        listView.setAdapter(myListAdapter);
        historyView.setAdapter(myHistoryAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                View detailsFrame = getActivity().findViewById(R.id.fragmentPhoto);
                String title = myListAdapter.getItem(position).getTitle();
                String url = myListAdapter.getItem(position).getUrl();
                // both fragments displayed
                if (detailsFrame != null && detailsFrame.getVisibility() == View.VISIBLE) {
                    mCallback.sendItem(myListAdapter.getItem(position));
                    myHistoryAdapter.addItem(myListAdapter.getItem(position));
                } else {
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), PhotoActivity.class);
                    intent.putExtra("title", title);
                    intent.putExtra("url", url);
                    myHistoryAdapter.addItem(new FlickrPhoto(title, url));
                    startActivity(intent);
                }
            }
        });
        historyView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                View detailsFrame = getActivity().findViewById(R.id.fragmentPhoto);
                String title = myHistoryAdapter.getItem(position).getTitle();
                String url = myHistoryAdapter.getItem(position).getUrl();
                // both fragments displayed
                if (detailsFrame != null && detailsFrame.getVisibility() == View.VISIBLE) {
                    mCallback.sendItem(myHistoryAdapter.getItem(position));
                } else {
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), PhotoActivity.class);
                    intent.putExtra("title", title);
                    intent.putExtra("url", url);
                    startActivity(intent);
                }
            }
        });
        editText = (EditText) view.findViewById(R.id.inp_search);

        Button changeButton = (Button) view.findViewById(R.id.btn_change);
        changeButton.setOnClickListener(this);
        if (mstButton == null) {
            mstButton = (MultiStateToggleButton) view.findViewById(R.id.mstb_multi_id);
            initLists();
        }
        mstButton.setOnValueChangedListener(new ToggleButton.OnValueChangedListener() {
            @Override
            public void onValueChanged(int position) {
                if (position == 0) {
                    historyView.setVisibility(View.GONE);
                    listView.setVisibility(View.VISIBLE);
                } else {
                    listView.setVisibility(View.GONE);
                    historyView.setVisibility(View.VISIBLE);
                }
            }
        });
        return view;
    }

    private void initLists() {
        mstButton.setValue(0);
        historyView.setVisibility(View.GONE);
        listView.setVisibility(View.VISIBLE);
    }

    public void onClick(View view) {
        if (bound) {
            initLists();
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
        if (list.size() == 0) {
            Toast.makeText(getActivity(), R.string.no_result, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onFailure() {
        Toast.makeText(getActivity(), R.string.request_failure, Toast.LENGTH_LONG).show();
    }

    public interface ItemClicked {
        void sendItem(FlickrPhoto photo);
    }
}
