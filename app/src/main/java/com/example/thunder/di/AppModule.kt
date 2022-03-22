package com.example.thunder.di

import android.app.Application
import androidx.room.Room
import com.example.thunder.data.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesDataBase(
        app: Application,
        callback: AppDatabase.Callback
    ) = Room.databaseBuilder(app, AppDatabase::class.java, "thunder_db")
        .fallbackToDestructiveMigration()
        .addCallback(callback)
        .build()

    @Provides
    fun providesWeatherDao(db: AppDatabase) = db.weatherDao()

    @Provides
    fun providesAlertDao(db: AppDatabase) = db.alertDao()

    @ApplicationScope
    @Provides
    @Singleton
    fun providesApplicationScope() =
        CoroutineScope(SupervisorJob())//when child of coroutine failed keep other child run

    @Retention(AnnotationRetention.RUNTIME)
    @Qualifier
    annotation class ApplicationScope
}