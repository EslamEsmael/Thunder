package com.example.thunder.ui.favourites

import com.example.thunder.model.WeatherResponse

interface ItemFavouriteClickListener {

    fun favItemClicked(weatherResponse: WeatherResponse)
}