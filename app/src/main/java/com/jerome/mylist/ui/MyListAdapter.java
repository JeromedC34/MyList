package com.jerome.mylist.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jerome.mylist.dat.FlickrPhoto;
import com.jerome.mylist.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

class MyListAdapter extends BaseAdapter {
    private List<FlickrPhoto> myList = new ArrayList<>();
    private Context context;
    private String MY_API_KEY = "";

    MyListAdapter(Context context) {
        this.context = context;
        MY_API_KEY = context.getResources().getString(R.string.flickr_api_key);
    }

    @Override
    public int getCount() {
        return myList.size();
    }

    @Override
    public FlickrPhoto getItem(int position) {
        return myList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.row_layout, parent, false);
        }
        TextView textView = (TextView) convertView.findViewById(R.id.text);
        textView.setText(getItem(position).getId() + " - " + getItem(position).getCount() + " - " + getItem(position).getType() + " - " + getItem(position).getSearch() + " - " + getItem(position).getTitle());
        ImageView imageView = (ImageView) convertView.findViewById(R.id.img);
        imageView.setTag("img_" + position);
        Picasso.with(context)
                .load(getItem(position).getUrl())
                .resize(100, 100)
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher)
                .into(imageView);
        return convertView;
    }

    void setList(List<FlickrPhoto> aList) {
        myList = aList;
        notifyDataSetChanged();
    }
    void addItem(FlickrPhoto newItem) {
        boolean found = false;
        for (FlickrPhoto anItem: myList) {
            if (newItem.getUrl().equals(anItem.getUrl())) {
                found = true;
            }
        }
        if (!found) {
            myList.add(newItem);
        }
        notifyDataSetChanged();
    }

}
