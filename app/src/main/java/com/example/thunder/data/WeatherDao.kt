package com.example.thunder.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.thunder.model.Weather
import com.example.thunder.model.WeatherResponse
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {

    @Query("SELECT * FROM WeatherData ")
    fun getAllWeatherResponse(): Flow<WeatherResponse>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(weather: WeatherResponse)

    @Query("Select * FROM WeatherData where isFavourite=:isFavourite")
    fun getStoredFavouritePlaces(isFavourite: Boolean = true): Flow<List<WeatherResponse>>

    @Query("Select * From WeatherData where isFavourite=:isFavourite")
    fun getStoredCurrentPlace(isFavourite: Boolean = false): Flow<List<WeatherResponse>>

    @Query("SELECT * FROM WeatherData  WHERE isFavourite=:isFavourite")
    fun getWeatherResponseAlert(isFavourite: Boolean = false): WeatherResponse
}