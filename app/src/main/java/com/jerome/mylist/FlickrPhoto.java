package com.jerome.mylist;

public class FlickrPhoto {
    String title;
    String url;

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
}
