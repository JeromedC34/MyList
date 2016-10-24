package com.jerome.mylist;


import android.content.Context;
import android.util.Log;

import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.List;

public class FlickrPhotoPersistenceManager {
    public FlickrPhotoPersistenceManager(Context context) {
        FlowManager.init(
                new FlowConfig.Builder(context).openDatabasesOnInit(true).build());
    }

    public List<FlickrPhoto> getFlickrPhotoByUrl(String string) {
        return SQLite.select()
                .from(FlickrPhoto.class)
                .where(FlickrPhoto_Table.url.like(string + "%"))
                .or(FlickrPhoto_Table.url.like("%" + string + "%"))
                .queryList();
    }

    public List<FlickrPhoto> getFlickrPhotoHistory() {
        return SQLite.select()
                .from(FlickrPhoto.class)
                .queryList();
    }

    public void save(FlickrPhoto dog) {
        try {
            List<FlickrPhoto> testExists = getFlickrPhotoByUrl(dog.getUrl());
            if (testExists.size() == 0) {
                dog.save();
            }
        } catch (Exception e) {
            Log.w("SaveFlickrPhoto", e.toString());
        }
    }
}