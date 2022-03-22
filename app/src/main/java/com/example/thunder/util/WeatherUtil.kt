package com.example.thunder.util

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.provider.Settings
import android.system.Os.close
import androidx.appcompat.app.AlertDialog
import com.google.android.gms.location.R
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

object WeatherUtil {

    fun getHourAndMinute(timestamp: Long, lang: String): String {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = (timestamp * 1000)
        val format = SimpleDateFormat("hh:00 aa", Locale(lang, lang.uppercase()))
        return format.format(calendar.time)
    }

    fun convertTimestampToDate(timestamp: Long, lang: String): String {
        val calendar: Calendar = Calendar.getInstance()
        calendar.timeInMillis = timestamp * 1000
        val month = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())
        val day = calendar.get(Calendar.DAY_OF_MONTH).toString()
        var year = calendar.get(Calendar.YEAR).toString()
        val format = SimpleDateFormat("MMM dd, yyyy", Locale(lang, lang.uppercase()))
        return format.format(calendar.time)
    }

    fun converterHourAndMinutes(timestamp: Long, lang: String): String {
        val formatter = DateTimeFormatter.ofPattern("HH:mm a", Locale(lang, lang.uppercase()));
        val instant = Instant.ofEpochMilli(timestamp)
        val date = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
        return formatter.format(date)
    }

    fun converterToDay(timestamp: Long, lang: String): String {
        val formatter = DateTimeFormatter.ofPattern("MMM, dd", Locale(lang, lang.uppercase()));
        val instant = Instant.ofEpochMilli(timestamp)
        val date = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
        return formatter.format(date)
    }

    fun checkForInternet(context: Context): Boolean {

        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val network = connectivityManager.activeNetwork ?: return false

        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

        return when {

            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true

            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true

            else -> false
        }
    }

    fun TwoButtonsDialogBuilder(
        context: Context?,
        title: String?,
        message: String?,
        positiveButtonText: String?,
        negativeButtonText: String?,
        positiveClick: DialogInterface.OnClickListener?,
        negativeClick: DialogInterface.OnClickListener?
    ): AlertDialog.Builder {
        val builder = AlertDialog.Builder(
            context!!
        )
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setNegativeButton(negativeButtonText, negativeClick)
        builder.setPositiveButton(positiveButtonText, positiveClick)
        return builder
    }

    fun getCurrentLanguage(context: Context): String {
        val sharedPreferencesFactory = SharedPreferencesFactory(context)
        return sharedPreferencesFactory.getLanguage().toString()
    }




}