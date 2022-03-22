package com.example.thunder.util.notifier

import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.BroadcastReceiver
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import com.example.thunder.MainActivity
import com.example.thunder.NotificationCenter
import com.example.thunder.R
import com.example.thunder.data.RepositoryInterface
import com.example.thunder.model.WeatherResponse
import com.example.thunder.util.SharedPreferencesFactory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class AlertNotificationReceiver : BroadcastReceiver() {
    @Inject
    lateinit var repository: RepositoryInterface
    override fun onReceive(context: Context, intent: Intent) {
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
        Log.d("AlertNotificationReceiver", "onReceive: Done")
        var weather: WeatherResponse
        val NOTIFICATION_ID = Random(System.currentTimeMillis()).nextInt(120)
        val alarmsound =
            Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + context.applicationContext.packageName + "/" + R.raw.messagetone)
        val intent1 = Intent(context, MainActivity::class.java)
        intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val taskStackBuilder = TaskStackBuilder.create(context)
        taskStackBuilder.addParentStack(MainActivity::class.java)
        taskStackBuilder.addNextIntent(intent1)
        val intent2: PendingIntent =
            taskStackBuilder.getPendingIntent(1, PendingIntent.FLAG_UPDATE_CURRENT)

        val sharedPreferencesFactory = SharedPreferencesFactory(context)

        CoroutineScope(Dispatchers.IO).launch {
            weather = repository.getWeatherForAlert()
            Log.d("TAG", "onReceive:Weather ${weather.timezone} ")

            if (sharedPreferencesFactory.getNotificationEnabledOrDisabled())
                NotificationCenter.showAlertNotification(context, intent2, weather)

            if (sharedPreferencesFactory.getAlertEnabledOrDisabled())
                CoroutineScope(Dispatchers.Main).launch {
                    showDialog(context, weather)
                }
        }

    }

    private fun showDialog(context: Context, weatherResponse: WeatherResponse) {
        val notificationDialogOverApp = NotificationDialogOverApp(context, weatherResponse)
        notificationDialogOverApp.open()
    }


}