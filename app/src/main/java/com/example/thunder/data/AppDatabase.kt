package com.example.thunder.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.thunder.di.AppModule
import com.example.thunder.model.AlertModel
import com.example.thunder.model.WeatherResponse
import com.example.weatherforcast.data.roomdb.Converters
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider


@Database(
    entities = [WeatherResponse::class,
        AlertModel::class], version = 1, exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun weatherDao(): WeatherDao
    abstract fun alertDao() : AlertDao

    class Callback @Inject constructor(
        private val dataBase: Provider<AppDatabase>,
        @AppModule.ApplicationScope private val applicationScope: CoroutineScope
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            val dao = dataBase.get().weatherDao()
            applicationScope.launch {
                // dao.insert()
            }
        }

    }

}