package com.example.thunder.util

import android.content.Context
import com.example.thunder.R
import kotlin.math.roundToInt

class UnitsConverters(var context: Context) {

    fun returnTemperatureUsingUserFormat(value: String): String {
        var result = value
        val sharedPreferencesFactory = SharedPreferencesFactory(context)
        val units = sharedPreferencesFactory.getUnitsOfTemperature()

        when {
            units.equals("K") -> {
                result = "$value \u212A"
            }
            units.equals("C") -> {
                val celsius: Float = value.toFloat() - 273.15f
                result = "${celsius.roundToInt()} \u2103"
            }
            units.equals("F") -> {
                val fah = 1.8 * (value.toFloat() - 273) + 32
                result = "${fah.roundToInt()} \u2109"
            }
        }
        return result
    }

    fun returnWindSpeedUsingUserFormat(value: String): String {
        var result = value
        val sharedPreferencesFactory = SharedPreferencesFactory(context)
        val units = sharedPreferencesFactory.getUnitsOfWindSpeed()
        if (units.equals("meter/sec")) {
            val unit = context.getString(R.string.metre_sec)
            result = "$value m/s"
        } else if (units.equals("miles/hour")) {
            val mph = value.toFloat().roundToInt() * 2.23694
            val unit = context.getString(R.string.mile_hour)
            result = "${mph.roundToInt()} $unit"
        }
        return result
    }

}