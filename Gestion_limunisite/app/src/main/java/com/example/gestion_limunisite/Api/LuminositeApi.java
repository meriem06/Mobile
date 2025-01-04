package com.example.gestion_limunisite.Api;

import com.example.gestion_limunisite.entity.Luminosite;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
public interface LuminositeApi {

    // Ajouter une nouvelle luminosité
    @POST("/luminosite/add")
    Call<Long> insert(@Body Luminosite luminosite);

    // Récupérer toutes les luminosités
    @GET("/luminosite/get")
    Call<List<Luminosite>> getAll();

    @DELETE("/luminosite/delete/{id}")
    Call<Void> deleteLuminosite(@Path("id") int id);

    // Mettre à jour une luminosité
    @PUT("/luminosite/update")
    Call<Void> updateLuminosite(@Body Luminosite luminosite);

    // Récupérer une luminosité par ID
    @GET("/luminosite/{id}")
    Call<Luminosite> getLuminositeById(@Path("id") long id);
}
