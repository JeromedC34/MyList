package com.jerome.mylist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class PhotoFragment extends Fragment {
    private static FlickrPhoto flickrPhoto = new FlickrPhoto();
    private TextView textView;
    private ImageView imageView;
    private ImageButton imageButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo, container, false);
        textView = (TextView) view.findViewById(R.id.photo_title);
        imageView = (ImageView) view.findViewById(R.id.photo_img);
        imageButton = (ImageButton) view.findViewById(R.id.photo_favorite);
        Bundle bundle = getArguments();
        if (bundle != null) {
            // value is set by Fragment arguments
            flickrPhoto = (FlickrPhoto) bundle.getSerializable("photo");
        } else {
            Activity activity = getActivity();
            Intent intent = null;
            if (activity != null) {
                intent = activity.getIntent();
            }
            if (activity != null && intent != null) {
                // value is read from activity intent
                flickrPhoto = (FlickrPhoto) intent.getSerializableExtra("photo");
            } else if (flickrPhoto != null) {
                // nothing to do, object already existing/set
            } else if (savedInstanceState != null) {
                // value can be restored after Fragment is restored
                flickrPhoto = (FlickrPhoto) savedInstanceState.getSerializable("photo");
            }
        }
        setPhoto(view, flickrPhoto);
        return view;
    }

    public void setPhoto(View view, FlickrPhoto photo) {
        flickrPhoto = photo;
        if (flickrPhoto != null && !"".equals(flickrPhoto.getUrl())) {
            textView = (TextView) view.findViewById(R.id.photo_title);
            textView.setText(photo.getTitle());
            imageView = (ImageView) view.findViewById(R.id.photo_img);
            Picasso.with(getContext())
                    .load(photo.getUrl())
                    .placeholder(R.mipmap.ic_launcher)
                    .into(imageView);
            if (photo.getType() == FlickrPhotoType.FAVORITE) {
                imageButton.setImageResource(android.R.drawable.btn_star_big_on);
            }
        }
    }
}
