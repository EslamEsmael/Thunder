package com.example.thunder.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.thunder.network.RetrofitHelper
import com.example.thunder.network.WeatherServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val lat = "31.2386733"
    private val lon = "32.2501499"
    private val appid = "8fd4f4da8e0b8e17fd886d2075700106"

    private var job: Job? = null

    val weatherServices = RetrofitHelper.getInstance().create(WeatherServices::class.java)
    // launching a new coroutine

    fun loadDate() {
        /*lifecycle.coroutineScope.launch {
            job = CoroutineScope(Dispatchers.IO).launch {
                val result = weatherServices.getWeatherStatsForLocation(lat, lon, appid, "standard")
                val response = result.body()
            }
        }*/
    }


}