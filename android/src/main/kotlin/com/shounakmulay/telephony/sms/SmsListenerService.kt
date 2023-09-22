package com.shounakmulay.telephony.sms

import android.annotation.TargetApi
import android.app.*
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.Uri
import android.os.*
import android.provider.Settings
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

import android.content.BroadcastReceiver

class SmsListenerService : Service() {
    private val smsReceiver = IncomingSmsReceiver()

    override fun onCreate() {
        super.onCreate()
        Log.d("SmsListenerService", "  onCreate")
        startForegroundApiLevels()

    }

    private fun startForegroundApiLevels() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {

            startMyOwnForeground()

        } else startForeground(1, Notification())
    }

    private val dialogReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == "show_dialog") {
                // Show the dialog here
                showNotificationAccessPermissionDialog()
            }
        }
    }

    private fun checkNotification(): Boolean {
        val notificationManager = NotificationManagerCompat.from(this)
        return notificationManager.areNotificationsEnabled()
    }

    private fun showNotificationAccessPermissionDialog() {
        val alertDialog = AlertDialog.Builder(applicationContext)
        alertDialog.setTitle("Notification Access Required")
        alertDialog.setMessage("This app requires notification access to function properly. Please grant the access in the app settings.")

        alertDialog.setPositiveButton("Open Settings") { _, _ ->
            openNotificationAccessSettings(this)
        }

        alertDialog.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }

        alertDialog.show()
    }

    fun openNotificationAccessSettings(context: Context) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Check if notifications are enabled
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1 && !notificationManager.areNotificationsEnabled()) {
            // Notifications are not enabled, prompt the user to open the notification settings
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // On Android Oreo and later, open the app's notification settings
                val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                intent.putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
            } else {
                // On older Android versions, open the system notification settings
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.data = Uri.parse("package:" + context.packageName)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

                context.startActivity(intent)
            }
        } else {
            startForegroundApiLevels()
//            startMyOwnForeground()
            // Notifications are enabled, perform your tasks here
            // You can perform tasks that require notification permissions here
        }
    }


    private fun startAlarm() {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmIntent = Intent(this, MyAlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            this,
            0,
            alarmIntent,
            PendingIntent.FLAG_IMMUTABLE
        )

        // Schedule the alarm to run your service every 15 minutes
        alarmManager.setRepeating(
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            SystemClock.elapsedRealtime(),
            15 * 60 * 1000, // 15 minutes in milliseconds
            pendingIntent
        )
    }


    @TargetApi(Build.VERSION_CODES.O)
    private fun startMyOwnForeground() {
        val NOTIFICATION_CHANNEL_ID = "example.permanence"
        val channelName = "Background Service"
        val chan = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            channelName,
            NotificationManager.IMPORTANCE_HIGH
        )
        chan.lightColor = Color.BLUE
        chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        val manager = (getSystemService(NOTIFICATION_SERVICE) as NotificationManager)
        manager.createNotificationChannel(chan)
        val notificationBuilder: NotificationCompat.Builder =
            NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
        val notification: Notification = notificationBuilder.setOngoing(true)
            .setContentTitle("App is running in background 333")
            .setPriority(NotificationManager.IMPORTANCE_HIGH)
            .setCategory(Notification.CATEGORY_SERVICE)
            .setSmallIcon(com.shounakmulay.telephony.R.drawable.ic_android_black_24dp)
            .setOngoing(true) // Make the notification persistent
            .setAutoCancel(false)
            .build()

        startForeground(2, notification)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("SmsListenerService", "  onStartCommand")


        // Use flutterEngine to initialize the MethodChannel


//        val filter = IntentFilter("show_dialog")
//        registerReceiver(dialogReceiver, filter)
//        registerSmsReceiver()

        startAlarm()
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        Log.d("SmsListenerService", "onBind ")
//        val intentFilter = IntentFilter()
//        intentFilter.addAction("android.provider.Telephony.SMS_RECEIVED")
//        registerReceiver(smsReceiver, intentFilter)
        return null
    }

    override fun onDestroy() {

        unregisterReceiver(smsReceiver)


//        startSmsListenerService(this)

        val broadcastIntent = Intent()
        broadcastIntent.action = "restartservice"
        broadcastIntent.setClass(this, MyAlarmReceiver::class.java)
        sendBroadcast(broadcastIntent)

        super.onDestroy()

    }

    private fun registerSmsReceiver() {
        val intentFilter = IntentFilter("android.provider.Telephony.SMS_RECEIVED")
        registerReceiver(smsReceiver, intentFilter)
    }

}
