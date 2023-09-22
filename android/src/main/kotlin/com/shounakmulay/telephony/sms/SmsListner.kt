//package com.shounakmulay.telephony.sms
//
//import android.app.Service
//import android.content.Intent
//import android.content.IntentFilter
//import android.os.IBinder
//
//class SmsListenerService : Service() {
//    private val smsReceiver = IncomingSmsReceiver()
//
//    override fun onCreate() {
//        super.onCreate()
//        val intentFilter = IntentFilter()
//        intentFilter.addAction("android.provider.Telephony.SMS_RECEIVED")
//        registerReceiver(smsReceiver, intentFilter)
//    }
//
//    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
//        return START_STICKY
//    }
//
//    override fun onBind(intent: Intent?): IBinder? {
//        return null
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        unregisterReceiver(smsReceiver)
//    }
//}
