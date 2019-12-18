package com.manoj.recyclerviewtest.api;

import com.manoj.recyclerviewtest.api.pojo.Data;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface api {
    String baseurl = "https://reqres.in/api/";
    //String imageurl = "https://image.tmdb.org/t/p/w200";
    String imageurl ="https://image.tmdb.org/t/p/original";

    @GET("users")
    Call<Data> getdata(
            @Query("page") int pagenumber,
            @Query("delay") int delay
    );
}
