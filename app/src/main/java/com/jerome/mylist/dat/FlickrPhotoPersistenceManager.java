package com.jerome.mylist.dat;

import android.content.Context;
import android.util.Log;

import com.jerome.mylist.mod.FlickrPhotoType;
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
                .where(FlickrPhoto_Table.url.eq(string))
                .queryList();
    }

    public List<FlickrPhoto> readAll() {
        return SQLite.select()
                .from(FlickrPhoto.class)
                .queryList();
    }

    public List<FlickrPhoto> readHistory() {
        return SQLite.select()
                .from(FlickrPhoto.class)
                .orderBy(FlickrPhoto_Table.id, false)
                .queryList();
    }

    public List<FlickrPhoto> readFavorites() {
        return SQLite.select()
                .from(FlickrPhoto.class)
                .where(FlickrPhoto_Table.type.eq(FlickrPhotoType.FAVORITE))
                .orderBy(FlickrPhoto_Table.count, false)
                .queryList();
    }

    public void update(FlickrPhoto photo) {
        save(photo);
    }

    public void delete(FlickrPhoto photo) {
        photo.delete();
    }

    public void read() {
        // TODO
    }

    public void save(FlickrPhoto photo) {
        try {
            photo.save();
        } catch (Exception e) {
            Log.w("SaveFlickrPhoto", e.toString());
        }
    }
}