package com.example.thunder.util

import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.core.content.ContextCompat.getSystemService
import com.google.android.gms.location.R
import java.util.*

fun String.capitalizeWords(): String = split(" ").map {
    it.replaceFirstChar { it ->
        if (it.isLowerCase()) it.titlecase(
            Locale.getDefault()
        ) else it.toString()
    }
}.joinToString(" ")

fun isOnline(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (connectivityManager != null) {
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                return true
            }
        }
    }
    return false
}

fun isLocationEnabled(context: Context): Boolean {
    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
}

fun checkOverlayPermission(context: Context) {
    if (!Settings.canDrawOverlays(context)) {
        context.getString(com.example.thunder.R.string.tuesday)
        val builder = WeatherUtil.TwoButtonsDialogBuilder(context,
            context.getString(com.example.thunder.R.string.EnableNotiDialog),
            context.getString(com.example.thunder.R.string.EnableNotiDialogContent),
            context.getString(com.example.thunder.R.string.yes),
            context.getString(com.example.thunder.R.string.close),
            { dialogInterface, i ->
                val myIntent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
                myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(myIntent)
            }
        ) { dialogInterface, i -> dialogInterface.dismiss() }
        builder.show()
    }
}



