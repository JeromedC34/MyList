package com.jerome.mylist.biz;

import com.jerome.mylist.mod.FlickrPhotosResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface FlickrService {
    @GET("services/rest/?method=flickr.photos.search&safe_search=1&per_page=50&" +
            "format=json&nojsoncallback=1&api_key=2ef592bfddc86f508550184ec706a2fc")
    Call<FlickrPhotosResponse> getPhotos(@Query("tags") String query);
}