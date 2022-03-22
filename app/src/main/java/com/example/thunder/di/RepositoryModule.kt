package com.example.thunder.di

import com.example.thunder.data.AlertDao
import com.example.thunder.data.Repository
import com.example.thunder.data.RepositoryInterface
import com.example.thunder.data.WeatherDao
import com.example.thunder.data.network.WeatherServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideMainRepository(
        retrofit: WeatherServices,
        weatherDao: WeatherDao,
        alertDao: AlertDao
    ): RepositoryInterface {
        return Repository(retrofit, weatherDao,alertDao)
    }
}