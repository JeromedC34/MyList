package com.jerome.mylist.biz;

import android.content.Context;

import com.jerome.mylist.dat.FlickrPhoto;
import com.jerome.mylist.dat.FlickrPhotoPersistenceManager;
import com.jerome.mylist.mod.FlickrPhotoType;

import java.util.List;

public class MyPhotos {
    FlickrPhotoPersistenceManager flickrPhotoPersistenceManager;

    public MyPhotos(Context context) {
        flickrPhotoPersistenceManager = new FlickrPhotoPersistenceManager(context);
    }

    public List<FlickrPhoto> getHistory() {
        return flickrPhotoPersistenceManager.readHistory();
    }

    public void save(FlickrPhoto photo) {
        List<FlickrPhoto> testExists = flickrPhotoPersistenceManager.getFlickrPhotoByUrl(photo.getUrl());
        if (testExists.size() == 0) {
            flickrPhotoPersistenceManager.save(photo);
        } else {
            flickrPhotoPersistenceManager.update(photo);
        }
    }

    public FlickrPhoto seen(FlickrPhoto photo) {
        photo.seen();
        save(photo);
        return photo;
    }

    public FlickrPhoto toggleFavorite(FlickrPhoto photo) {
        if (photo.isFavorite()) {
            photo.setType(FlickrPhotoType.HISTORY);
        } else {
            photo.setType(FlickrPhotoType.FAVORITE);
        }
        save(photo);
        return photo;
    }
}
