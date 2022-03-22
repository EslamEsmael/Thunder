package com.example.thunder.data

import com.example.thunder.model.AlertModel
import com.example.thunder.model.Weather
import com.example.thunder.model.WeatherResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface RepositoryInterface {

    suspend fun getCurrentWeatherAPI(
        lat: Double,
        lon: Double,
        currentCity: String
    ): Response<WeatherResponse>

    suspend fun insertFavLocation(weatherResponse: WeatherResponse)

    suspend fun insertCurrentLocation(weatherResponse: WeatherResponse)

    fun getStoredFavouritePlaces(): Flow<List<WeatherResponse>>

    fun getCurrentLocation(): Flow<List<WeatherResponse>>

    suspend fun insertAlert(alert: AlertModel): Long
    suspend fun deleteAlert(alert: AlertModel)
    fun getAlerts(): Flow<List<AlertModel>>
    fun getWeatherForAlert(): WeatherResponse

}