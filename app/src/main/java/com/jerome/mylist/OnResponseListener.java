package com.jerome.mylist;

import java.util.List;

public interface OnResponseListener {
    void onResponse(List<FlickrPhoto> list);
    void onFailure();
}
