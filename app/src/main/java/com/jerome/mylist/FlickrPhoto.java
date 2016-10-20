package com.jerome.mylist;

public class FlickrPhoto {
    String title;
    String url;

    public FlickrPhoto() {
        title = "";
        url = "";
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

    public String getUrl() {
        return url;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
