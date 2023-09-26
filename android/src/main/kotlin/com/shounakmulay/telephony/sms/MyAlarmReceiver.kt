package com.shounakmulay.telephony.sms

import android.app.ActivityManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log


class MyAlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        // Start your service here
//        if (context != null &&!isServiceRunning(context, SmsListenerService::class.java)) {
//            val serviceIntent = Intent(context, SmsListenerService::class.java)
//            Log.d("MyAlarmReceiver", "onReceive")
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                context.startForegroundService(serviceIntent)
//            } else {
//                Log.d("MyAlarmReceiver", "MyAlarmReceiver onReceive")
//                context.startService(serviceIntent)
//            }
//        }
    }

    private fun isServiceRunning(context: Context, serviceClass: Class<*>): Boolean {
        val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
        return false
    }

}
