package com.example.thunder.data

import androidx.room.*
import com.example.thunder.model.AlertModel
import kotlinx.coroutines.flow.Flow

@Dao
interface AlertDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insetAlert(alertModel: AlertModel): Long

    @Delete
    suspend fun deleteAlert(alertModel: AlertModel)

    @Query("SELECT * FROM alerts")
    fun getAlerts(): Flow<List<AlertModel>>
}