package com.jerome.mylist.biz;

import com.google.android.gms.maps.model.LatLng;

public interface OnGeoLocListener {
    void onGeoLocUpdate(LatLng newLatLng, String string);
}
