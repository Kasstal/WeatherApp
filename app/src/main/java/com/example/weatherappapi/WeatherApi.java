package com.example.weatherappapi;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherApi {
    @GET("forecast")
    Call<WeatherResponse> getWeather(
            @Query("latitude") double lat,
            @Query("longitude") double lon,
            @Query("current") String params
    );
}
