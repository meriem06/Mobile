package com.example.usermanagmentecotracker.JihedPackage.api;

import com.example.usermanagmentecotracker.JihedPackage.Entity.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UserApi {

    @POST("/users/login")
    Call<User> login(@Query("email") String email, @Query("password") String password);

    @POST("/users/register")
    Call<String> registerUser(@Body User user);

    @PUT("/users/update-username/{id}")
    Call<String> updateUsername(@Path("id") int id, @Query("name") String name);

    @PUT("/users/update-email/{id}")
    Call<String> updateEmail(@Path("id") int id, @Query("newEmail") String newEmail);

    @PUT("/users/update-birthdate/{id}")
    Call<String> updateBirthdate(@Path("id") int id, @Query("birthdate") String birthdate);

    @PUT("/users/update-password/{id}")
    Call<String> updatePassword(@Path("id") int id, @Query("password") String password);

    @PUT("/users/update-image/{id}")
    Call<String> updateImagePath(@Path("id") int id, @Query("imagePath") String imagePath);
}
