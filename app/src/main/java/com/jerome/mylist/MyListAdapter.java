package com.jerome.mylist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MyListAdapter extends BaseAdapter {
    List<String> myList = new ArrayList<>();
    Context mContext;

    public MyListAdapter(Context context) {
        mContext = context;
    }

    @Override
    public int getCount() {
        return myList.size();
    }

    @Override
    public Object getItem(int position) {
        return myList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext)
                    .inflate(R.layout.row_layout, parent, false);
        }
        TextView textView = (TextView) convertView.findViewById(R.id.text);
        textView.setText(getItem(position).toString());
        return convertView;
    }

    public void setList(List<String> aList) {
        myList = aList;
        notifyDataSetChanged();
    }
}
