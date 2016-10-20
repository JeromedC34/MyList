package com.jerome.mylist;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class PhotoFragment extends Fragment {
    private static FlickrPhoto flickrPhoto;
    private TextView textView;
    private ImageView imageView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo, container, false);
        textView = (TextView) view.findViewById(R.id.photo_title);
        imageView = (ImageView) view.findViewById(R.id.photo_img);
        String title = "";
        String url = "";
        if (savedInstanceState != null) {
            // value can be restored after Fragment is restored
            title = savedInstanceState.getString("title");
            url = savedInstanceState.getString("url");
        } else if (getArguments() != null) {
            // value is set by Fragment arguments
            title = getArguments().getString("title");
            url = getArguments().getString("url");
        } else if (getActivity() != null && getActivity().getIntent() != null) {
            // value is read from activity intent
            title = getActivity().getIntent().getStringExtra("title");
            url = getActivity().getIntent().getStringExtra("url");
        } else {
            if (flickrPhoto != null) {
                title = flickrPhoto.getTitle();
                url = flickrPhoto.getUrl();
            }
        }
        setPhoto(view, new FlickrPhoto(title, url));
        return view;
    }

    public void setPhoto(View view, FlickrPhoto photo) {
        flickrPhoto = photo;
        textView = (TextView) view.findViewById(R.id.photo_title);
        textView.setText(photo.getTitle());
        imageView = (ImageView) view.findViewById(R.id.photo_img);
        Picasso.with(getContext())
                .load(photo.getUrl())
                .placeholder(R.mipmap.ic_launcher)
                .into(imageView);
    }
}
