package com.example.thunder.util.notifier

import android.content.Context

import android.widget.TextView
import android.graphics.PixelFormat
import android.util.Log
import android.view.*
import android.widget.ImageView
import com.example.thunder.R
import com.example.thunder.model.WeatherResponse

import java.lang.Exception

class NotificationDialogOverApp(private val context: Context, weatherResponse: WeatherResponse?) {
    private val mView: View
    private val mParams: WindowManager.LayoutParams
    private val mWindowManager: WindowManager
    private val layoutInflater: LayoutInflater
    private val weather_ImageView: ImageView
    var cityNameTextView: TextView
    var descriptionWeatherTextView: TextView
    var detailsTextView: TextView
    fun open() {
        try {
            if (mView.windowToken == null) {
                if (mView.parent == null) {
                    mWindowManager.addView(mView, mParams)
                }
            }
        } catch (e: Exception) {
            Log.d("Error1", e.toString())
        }
    }

    private fun close() {
        try {
            (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).removeView(mView)
            mView.invalidate()
            (mView.parent as ViewGroup).removeAllViews()
        } catch (e: Exception) {
            Log.d("Error2", e.toString())
        }
    }

    init {
        mParams = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT

        )
        layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        mView = layoutInflater.inflate(R.layout.dialog_alert_overapp, null)
        cityNameTextView = mView.findViewById(R.id.cityName_textview)
        descriptionWeatherTextView = mView.findViewById(R.id.description_weather_TextView)
        detailsTextView = mView.findViewById(R.id.weather_details_TextView)
        weather_ImageView = mView.findViewById(R.id.weather_ImageView)
        weather_ImageView.setBackgroundResource(R.drawable.testimgae)
        cityNameTextView.text = weatherResponse?.timezone
        "${context.getString(R.string.it_seem)} ${
            weatherResponse?.current?.weather?.get(0)?.description ?: context.getString(R.string.no_alert)
        }".also { descriptionWeatherTextView.text = it }
        ("${context.getString(R.string.some_details)} \n${context.getString(R.string.temperature)} ${
            context.getString(
                R.string.colon
            )
        }" + (weatherResponse?.current?.temp) + "\n${
            context.getString(R.string.humidity)
        }${context.getString(R.string.colon)}" + (weatherResponse?.current?.humidity) + "\n${
            context.getString(
                R.string.wind_speed
            )
        }${context.getString(R.string.colon)} " + (weatherResponse?.current?.wind_speed)).also { detailsTextView.text = it }

        mView.findViewById<View>(R.id.close_button).setOnClickListener { view: View? -> close() }
        mParams.gravity = Gravity.CENTER
        mWindowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    }
}