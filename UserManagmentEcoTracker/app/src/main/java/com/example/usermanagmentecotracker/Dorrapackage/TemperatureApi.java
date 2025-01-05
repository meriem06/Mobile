package com.example.usermanagmentecotracker.Dorrapackage;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface TemperatureApi {
    @POST("/temperature/add")
    Call<Void> sendTemperature(@Body TemperatureEntry temperatureEntry);
}
