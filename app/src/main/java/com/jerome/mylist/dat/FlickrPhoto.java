package com.jerome.mylist.dat;

import com.google.android.gms.maps.model.LatLng;
import com.jerome.mylist.mod.FlickrPhotoType;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.annotation.Unique;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.io.Serializable;

@Table(database = AppDatabase.class)
public class FlickrPhoto extends BaseModel implements Serializable {
    @Column
    String title;
    @Column
    @Unique
    String url;
    @Column
    FlickrPhotoType type;
    @Column
    long count;
    @Column
    String search;
    @Column
    double lat;
    @Column
    double lon;
    @Column
    @PrimaryKey(autoincrement = true)
    private long id;

    public FlickrPhoto() {
        title = "";
        url = "";
        type = FlickrPhotoType.DEFAULT;
        count = 0;
        search = "";
        lat = Double.MAX_VALUE;
        lon = Double.MAX_VALUE;
    }

    public FlickrPhoto(String title, String url) {
        this(title, url, "");
    }

    public FlickrPhoto(String title, String url, String search) {
        this.title = title;
        this.url = url;
        this.type = FlickrPhotoType.HISTORY;
        this.count = 0;
        this.search = search;
        lat = Double.MAX_VALUE;
        lon = Double.MAX_VALUE;
    }

    public void setLatLng(LatLng latLng) {
        if (latLng != null && lat == Double.MAX_VALUE && lon == Double.MAX_VALUE) {
            lat = latLng.latitude;
            lon = latLng.longitude;
        }
    }

    public void seen() {
        this.count++;
    }

    @Override
    public String toString() {
        return "FlickrPhoto{" +
                "title='" + title + '\'' +
                ", url='" + url + '\'' +
                ", type=" + type +
                ", count=" + count +
                ", search='" + search + '\'' +
                ", lat=" + lat +
                ", lon=" + lon +
                ", id=" + id +
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

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public boolean isFavorite() {
        return type == FlickrPhotoType.FAVORITE;
    }
}
