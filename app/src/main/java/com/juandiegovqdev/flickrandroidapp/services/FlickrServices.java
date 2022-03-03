package com.juandiegovqdev.flickrandroidapp.services;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface FlickrServices {

    @GET("photos_public.gne?format=json")
    Observable<String> requestForPosts(@Query("tags") String tag);

}
