package com.example.usermanagmentecotracker.JihedPackage.api;

import com.example.usermanagmentecotracker.JihedPackage.Entity.Consommation;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;

import java.util.List;

public interface ConsommationApi {

    @POST("/consommations/add")
    Call<String> addConsommation(@Body Consommation consommation);

    @DELETE("/consommations/delete-all")
    Call<String> deleteAllConsommations();

    @GET("/consommations/sorted-by-cost")
    Call<List<Consommation>> getConsommationsSortedByCost();

    @GET("/consommations/sorted-by-distance")
    Call<List<Consommation>> getConsommationsSortedByDistance();
}
