package com.softwarica.heroapi;

import android.view.Display;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface Interface {
    @GET("heroes")
    Call<List<Model>> getAllData();


    @POST("heroes")
    Call<Void> setData(@Body Model data);
}
