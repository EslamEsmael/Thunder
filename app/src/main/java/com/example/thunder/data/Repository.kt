package com.example.thunder.data

import com.example.thunder.model.AlertModel
import com.example.thunder.model.WeatherResponse
import com.example.thunder.data.network.WeatherServices
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject

@Module
@InstallIn(SingletonComponent::class)
class Repository @Inject constructor(
    private val weatherServices: WeatherServices,
    private val weatherDao: WeatherDao,
    private var alertDao: AlertDao
) : RepositoryInterface {

    override suspend fun getCurrentWeatherAPI(
        lat: Double,
        lon: Double,
        currentCity: String
    ): Response<WeatherResponse> {
        return weatherServices.getWeatherStatsForLocation(lat, lon)
    }

    override suspend fun insertFavLocation(weatherResponse: WeatherResponse) {
        weatherDao.insert(weatherResponse)
    }

    override suspend fun insertCurrentLocation(weatherResponse: WeatherResponse) {
        weatherDao.insert(weatherResponse.apply { isFavourite = false })
    }

    override fun getStoredFavouritePlaces() =
        weatherDao.getStoredFavouritePlaces()

    override fun getCurrentLocation() =
        weatherDao.getStoredCurrentPlace()

    override suspend fun insertAlert(alert: AlertModel) =
        alertDao.insetAlert(alert)


    override suspend fun deleteAlert(alert: AlertModel) {
        alertDao.deleteAlert(alert)
    }

    override fun getAlerts(): Flow<List<AlertModel>> = alertDao.getAlerts()

    override fun getWeatherForAlert(): WeatherResponse =
        weatherDao.getWeatherResponseAlert(false)

}