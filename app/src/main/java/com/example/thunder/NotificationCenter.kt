package com.example.thunder

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentResolver
import android.content.Context
import android.graphics.BitmapFactory
import android.media.AudioAttributes
import android.net.Uri
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.thunder.model.WeatherResponse
import java.util.*

object NotificationCenter {
    fun showAlertNotification(
        context: Context,
        intent: PendingIntent?,
        weatherResponse: WeatherResponse
    ) {
        Log.d("TAG", "showMedicationReminder: ")
        val alarmsound =
            Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + context.applicationContext.packageName + "/" + R.raw.messagetone)
        val builder = NotificationCompat.Builder(context)
        var channel: NotificationChannel? = null
        val audioAttributesFactory = AudioAttributes.Builder()
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .setUsage(AudioAttributes.USAGE_NOTIFICATION)
            .build()
        channel =
            NotificationChannel("my_channel_01", "Medicine", NotificationManager.IMPORTANCE_HIGH)
        channel.setSound(alarmsound, audioAttributesFactory)
        channel.enableLights(true)
        channel.enableVibration(true)
        val notification = builder
            .setContentTitle("Weather Alert Condition For : " + weatherResponse.timezone)
            .setStyle(
                NotificationCompat.BigTextStyle().bigText(
                    "Weather for " + weatherResponse.timezone
                            + " is " + weatherResponse.current.weather[0].description
                )
            )
            .setAutoCancel(true)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setLargeIcon(
                BitmapFactory.decodeResource(
                    context.resources,
                    R.drawable.testimgae
                )
            )
            .setContentIntent(intent)
            .setSound(alarmsound)
            .setChannelId("my_channel_01")
            .build()
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
        notificationManager.notify(Random(System.currentTimeMillis()).nextInt(120), notification)
    }
}