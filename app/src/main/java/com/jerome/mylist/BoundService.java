package com.jerome.mylist;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BoundService extends Service {
    private final IBinder binder = new ServiceBinder();
    private FlickrService service;
    private OnResponseListener onResponseListener;

    public void setOnResponseListener(OnResponseListener onResponseListener) {
        this.onResponseListener = onResponseListener;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.flickr.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(FlickrService.class);
        return binder;
    }

    public String getImageURL(String farm, String server, String id, String secret) {
        return "https://farm" + farm + ".static.flickr.com/" + server + "/" + id + "_" + secret + ".jpg";
    }

    public void getPhotos(String query) {
        Call<FlickrPhotosResponse> flickrPhotosResponseCall = service.getPhotos(query);
        flickrPhotosResponseCall.enqueue(new Callback<FlickrPhotosResponse>() {
            @Override
            public void
            onResponse(Call<FlickrPhotosResponse> call, Response<FlickrPhotosResponse> response) {
                if (response.isSuccessful()) {
                    FlickrPhoto flickrPhoto;
                    List<FlickrPhotosResponse.Photos.Photo> photo = response.body().getPhotos().getPhoto();
                    List<FlickrPhoto> listFlickrPhoto = new ArrayList<>();
                    for (int i = 0; i < photo.size(); i++) {
                        flickrPhoto = new FlickrPhoto(photo.get(i).getTitle(),
                                getImageURL(photo.get(i).getFarm(),
                                        photo.get(i).getServer(),
                                        photo.get(i).getId(),
                                        photo.get(i).getSecret()));
                        listFlickrPhoto.add(flickrPhoto);
                    }
                    onResponseListener.onResponse(listFlickrPhoto);
                } else {
                    Log.e("GET_error", "error / onResponse / enqueue");
                }
            }

            @Override
            public void onFailure(Call<FlickrPhotosResponse> call, Throwable t) {
                onResponseListener.onFailure();
            }
        });
    }

    public class ServiceBinder extends Binder {
        BoundService getService() {
            return BoundService.this;
        }
    }
}