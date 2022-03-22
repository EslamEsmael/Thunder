package com.example.thunder.ui.settings

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.thunder.MainActivity
import com.example.thunder.util.SharedPreferencesFactory
import java.util.*

class SettingsViewModel : ViewModel() {


    fun saveTemperatureUnitInSharedPreferences(unit: String, context: Context) {
        val spFactory = SharedPreferencesFactory(context)

        spFactory.setUnitsOfTemperature(unit)
    }

    fun saveWindSpeedUnitInSharedPreferences(unit: String, context: Context) {
        val spFactory = SharedPreferencesFactory(context)
        spFactory.setUnitsOfUnitOfWindSpeed(unit)
    }

    fun saveNotificationEnabledOrDisabledInSharedPreferences(unit: Boolean, context: Context) {
        val spFactory = SharedPreferencesFactory(context)
        spFactory.setNotificationEnabledOrDisabled(unit)
    }

    fun saveLanguageInSharedPreferences(language: String, context: Context) {
        val spFactory = SharedPreferencesFactory(context)
        spFactory.setLanguage(language)
    }

    fun saveAlertEnabledOrDisabledInSharedPreferences(unit: Boolean, context: Context) {
        val spFactory = SharedPreferencesFactory(context)
        spFactory.setAlertEnabledOrDisabled(unit)
    }

    fun updateApplicationLanguage(language: String, activity: Activity) {
        val local = Locale(language)
        val res = activity.resources
        val dm = res.displayMetrics
        val config = res.configuration
        res.updateConfiguration(config, dm)
        val refresh = Intent(activity, MainActivity::class.java)
        activity.startActivity(refresh)
    }

}