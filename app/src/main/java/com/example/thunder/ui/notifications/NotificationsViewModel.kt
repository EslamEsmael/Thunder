package com.example.thunder.ui.notifications

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.thunder.data.RepositoryInterface
import com.example.thunder.model.AlertModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationsViewModel@Inject constructor(
    private val repository: RepositoryInterface
) : ViewModel() {

    fun getAlerts(): Flow<List<AlertModel>> {
        return repository.getAlerts()
    }

    fun deleteAlert(alert: AlertModel) {
        CoroutineScope(Dispatchers.IO).launch {
            repository.deleteAlert(alert)
        }
    }

}