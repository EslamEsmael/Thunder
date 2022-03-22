package com.example.thunder.ui.favourites

import androidx.lifecycle.ViewModel
import com.example.thunder.data.RepositoryInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MapsFragmentViewModel @Inject constructor(private val repo: RepositoryInterface) :
    ViewModel() {

    suspend fun insertFavLocation(lat: Double, lng: Double) {
        //todo get from weather api
        //todo insert into room

        val weatherResponse = repo.getCurrentWeatherAPI(lat, lng, "asdasd")
        repo.insertFavLocation(weatherResponse.body()?.apply {
            isFavourite = true
        }!!)

    }

}