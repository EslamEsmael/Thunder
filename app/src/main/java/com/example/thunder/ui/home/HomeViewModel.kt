package com.example.thunder.ui.home

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.thunder.data.RepositoryInterface
import com.example.thunder.model.WeatherResponse
import com.example.thunder.util.SharedPreferencesFactory
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repo: RepositoryInterface) : ViewModel() {

    suspend fun makeRequest(lat: Double, lon: Double, currentCity: String) =
        repo.getCurrentWeatherAPI(lat, lon, currentCity)

    fun saveLocationToSharedPreferences(latLng: LatLng, context: Context) {
        val sharedPreferencesFactory = SharedPreferencesFactory(context);
        sharedPreferencesFactory.setLatLng(latLng);
    }

    fun getSavedCurrentPlace() = repo.getCurrentLocation()

    suspend fun saveCurrentLocationWeatherResponse(weatherResponse: WeatherResponse) {
        repo.insertCurrentLocation(weatherResponse.apply {
            isFavourite = false
        })
    }
}