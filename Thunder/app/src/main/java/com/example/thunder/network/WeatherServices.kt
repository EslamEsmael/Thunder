package com.example.thunder.network

import com.example.example.ResponseModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherServices {
    // https://api.openweathermap.org/data/2.5/onecall?
    // lat=31.2653
    // &lon=32.3019
    // &exclude=minutely
    // &appid=c8fb7ff8299135714c6222d1cebd1e0e
    // &untis=standard

    @GET("onecall?")
    suspend fun getWeatherStatsForLocation(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("appid") appid: String,
        @Query("units") units: String
    ): Response<ResponseModel>

}