package com.example.weatherappapi;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GeoApi {
    @GET("search")
    Call<GeoResponse> getCoordinates(
            @Query("name") String cityName,
            @Query("count") int count,
            @Query("language") String lang,
            @Query("format") String format
    );
}
