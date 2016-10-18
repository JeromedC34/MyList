package com.jerome.mylist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MyListAdapter extends BaseAdapter {
    List<FlickrPhoto> myList = new ArrayList<>();
    Context context;
    private String MY_API_KEY = "2ef592bfddc86f508550184ec706a2fc";

    public MyListAdapter(Context context) {
        this.context = context;
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
        textView.setText(getItem(position).getTitle());
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

    public void setList(List<FlickrPhoto> aList) {
        myList = aList;
        notifyDataSetChanged();
    }
}
