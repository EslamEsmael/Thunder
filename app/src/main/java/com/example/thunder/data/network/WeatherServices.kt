package com.example.thunder.data.network

import com.example.thunder.model.WeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


private val API_KEY: String = "c8fb7ff8299135714c6222d1cebd1e0e"

interface WeatherServices {
    // https://api.openweathermap.org/data/2.5/onecall?
    // lat=31.2653
    // &lon=32.3019
    // &exclude=minutely
    // &appid=c8fb7ff8299135714c6222d1cebd1e0e
    // &untis=standard


    @GET("onecall?")
    suspend fun getWeatherStatsForLocation(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("units") unit: String = "standard",
        @Query("appid") appid: String = API_KEY
    ): Response<WeatherResponse>

}