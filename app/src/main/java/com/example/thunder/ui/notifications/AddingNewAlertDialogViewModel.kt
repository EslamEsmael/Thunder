package com.example.thunder.ui.notifications

import androidx.lifecycle.ViewModel
import com.example.thunder.data.RepositoryInterface
import com.example.thunder.model.AlertModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddingNewAlertDialogViewModel @Inject constructor(
    val repository: RepositoryInterface
) : ViewModel() {

    suspend fun insertAlert(alert: AlertModel): Long =
        repository.insertAlert(alert)


}