package com.jerome.mylist.biz;

import com.jerome.mylist.dat.FlickrPhoto;

import java.util.List;

public interface OnResponseListener {
    void onResponse(List<FlickrPhoto> list);
    void onFailure();
}
