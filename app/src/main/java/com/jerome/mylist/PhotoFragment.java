package com.jerome.mylist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class PhotoFragment extends Fragment {
    private static FlickrPhoto flickrPhoto = new FlickrPhoto();
    private TextView textView;
    private ImageView imageView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo, container, false);
        textView = (TextView) view.findViewById(R.id.photo_title);
        imageView = (ImageView) view.findViewById(R.id.photo_img);
        String lTitle = "";
        String lUrl = "";
        Bundle bundle = getArguments();
        if (bundle != null) {
            // value is set by Fragment arguments
            lTitle = bundle.getString("title");
            lUrl = bundle.getString("url");
        } else {
            Activity activity = getActivity();
            Intent intent = null;
            if (activity != null) {
                intent = activity.getIntent();
            }
            if (activity != null && intent != null) {
                // value is read from activity intent
                lTitle = intent.getStringExtra("title");
                lUrl = intent.getStringExtra("url");
            } else if (flickrPhoto != null) {
                lTitle = flickrPhoto.getTitle();
                lUrl = flickrPhoto.getUrl();
            } else if (savedInstanceState != null) {
                // value can be restored after Fragment is restored
                lTitle = savedInstanceState.getString("title");
                lUrl = savedInstanceState.getString("url");
            }
        }
        // if we've gotten something then we use it
        if ((!"".equals(lTitle) && lTitle != null) || (!"".equals(lUrl) && lUrl != null)) {
            flickrPhoto.setTitle(lTitle);
            flickrPhoto.setUrl(lUrl);
        }
        setPhoto(view, flickrPhoto);
        return view;
    }

    public void setPhoto(View view, FlickrPhoto photo) {
        flickrPhoto = photo;
        if (!"".equals(flickrPhoto.getUrl())) {
            textView = (TextView) view.findViewById(R.id.photo_title);
            textView.setText(photo.getTitle());
            imageView = (ImageView) view.findViewById(R.id.photo_img);
            Picasso.with(getContext())
                    .load(photo.getUrl())
                    .placeholder(R.mipmap.ic_launcher)
                    .into(imageView);
        }
    }
}
