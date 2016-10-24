package com.jerome.mylist;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.io.Serializable;

@Table(database = AppDatabase.class)
public class FlickrPhoto extends BaseModel implements Serializable {
    @Column
    @PrimaryKey(autoincrement = true)
    private long id;
    @Column
    String title;
    @Column
    String url;
    @Column
    FlickrPhotoType type;
    @Column
    long count;
    @Column
    String search;
    @Column
    long lat;
    @Column
    long lon;

    public FlickrPhoto() {
        title = "";
        url = "";
        type = FlickrPhotoType.HISTORY;
        count = 0;
        search = "";
        lat = 0;
        lon = 0;
    }

    public FlickrPhoto(String title, String url) {
        this.title = title;
        this.url = url;
    }

    @Override
    public String toString() {
        return "FlickrPhoto{" +
                "title='" + title + '\'' +
                ", url='" + url + '\'' +
                '}';
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public FlickrPhotoType getType() {
        return type;
    }

    public void setType(FlickrPhotoType type) {
        this.type = type;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public long getLat() {
        return lat;
    }

    public void setLat(long lat) {
        this.lat = lat;
    }

    public long getLon() {
        return lon;
    }

    public void setLon(long lon) {
        this.lon = lon;
    }
}
