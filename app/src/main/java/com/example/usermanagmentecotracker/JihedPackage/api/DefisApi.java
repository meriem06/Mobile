package com.example.usermanagmentecotracker.JihedPackage.api;

import com.example.usermanagmentecotracker.JihedPackage.Entity.Defis;

import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface DefisApi {


    @POST("/defis/add")
    Call<String> addDefis(@Body Defis defis);


    @PUT("/defis/update-is-completed/{id}")
    Call<String> updateIsCompleted(@Path("id") long id, @Body Boolean isCompleted);

    @GET("/defis/all")
    Call<List<Defis>> getAllDefis();

    @DELETE("/defis/delete/{id}")
    Call<String> deleteDefis(@Path("id") long id);
}
