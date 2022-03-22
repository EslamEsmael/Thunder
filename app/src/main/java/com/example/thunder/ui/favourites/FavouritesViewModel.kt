package com.example.thunder.ui.favourites

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.Navigation
import com.example.thunder.R
import com.example.thunder.data.RepositoryInterface
import com.example.thunder.model.WeatherResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FavouritesViewModel @Inject constructor(private val repo: RepositoryInterface) : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is dashboard Fragment"
    }
    val text: LiveData<String> = _text

    fun navigateToFavPlacesMap(view: View) {
        Navigation.findNavController(view)
            .navigate(R.id.action_navigation_favourite_to_navigation_map)
    }

    fun navigateToPlaceDetails(view: View, weatherResponse: WeatherResponse) {

        val bundle = Bundle()
        bundle.putSerializable("current", weatherResponse)
        Navigation.findNavController(view)
            .navigate(R.id.action_navigation_favourite_to_place_details, bundle)
    }

    fun getSavedFavouritePlaces() = repo.getStoredFavouritePlaces()
}