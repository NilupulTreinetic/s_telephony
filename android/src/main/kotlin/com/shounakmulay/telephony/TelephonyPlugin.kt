package com.shounakmulay.telephony

import android.app.ActivityManager
import com.shounakmulay.telephony.sms.SmsListenerService
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.NonNull
import com.shounakmulay.telephony.utils.Constants.CHANNEL_SMS
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.*
import android.util.Log
import com.shounakmulay.telephony.sms.*

class TelephonyPlugin : FlutterPlugin, ActivityAware {

  companion object {
    private var isPluginInitialized = false
  }

  private lateinit var smsChannel: MethodChannel

  private lateinit var smsMethodCallHandler: SmsMethodCallHandler

  private lateinit var smsController: SmsController

  private lateinit var binaryMessenger: BinaryMessenger

  private lateinit var permissionsController: PermissionsController
  private val SERVICE_MANAGE_CHANNEL = "manage_service"

  override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    if (!this::binaryMessenger.isInitialized) {
      binaryMessenger = flutterPluginBinding.binaryMessenger
      setupPlugin(flutterPluginBinding.applicationContext, binaryMessenger)

    }
    Log.d("TelephonyPlugin","onAttachedToEngine---->")
    if (!isPluginInitialized) {
//      binaryMessenger = flutterPluginBinding.binaryMessenger
//      setupPlugin(flutterPluginBinding.applicationContext, binaryMessenger)
      isPluginInitialized = true
    }
//    setupPlugin(flutterPluginBinding.applicationContext, binaryMessenger)
  }

  override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
    Log.d("TelephonyPlugin","onDetachedFromEngine ---->")
    isPluginInitialized = false
    tearDownPlugin()
  }

  override fun onDetachedFromActivity() {
    Log.d("TelephonyPlugin","onDetachedFromActivity ---->")
    isPluginInitialized = false

    tearDownPlugin()
  }

  override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
    Log.d("TelephonyPlugin","onReattachedToActivityForConfigChanges ---->")

    onAttachedToActivity(binding)
  }

  override fun onAttachedToActivity(binding: ActivityPluginBinding) {
    Log.d("TelephonyPlugin","onAttachedToActivity ---->")

    IncomingSmsReceiver.foregroundSmsChannel = smsChannel
    smsMethodCallHandler.setActivity(binding.activity)
    binding.addRequestPermissionsResultListener(smsMethodCallHandler)

  }

  override fun onDetachedFromActivityForConfigChanges() {
    Log.d("TelephonyPlugin","onDetachedFromActivityForConfigChanges ---->")

    onDetachedFromActivity()
  }

  private fun setupPlugin(context: Context, messenger: BinaryMessenger) {
    smsController = SmsController(context)
    permissionsController = PermissionsController(context)
    smsMethodCallHandler = SmsMethodCallHandler(context, smsController, permissionsController)

    smsChannel = MethodChannel(messenger, CHANNEL_SMS)
    smsChannel.setMethodCallHandler(smsMethodCallHandler)
    smsMethodCallHandler.setForegroundChannel(smsChannel)

    MethodChannel(messenger, SERVICE_MANAGE_CHANNEL).setMethodCallHandler { call, result ->
      if (call.method == "startSmsDetectionService") {
//        startSmsListenerService(context)
      } else {
        result.notImplemented()
      }
    }


    Log.d("Set plugin", "call set plugin method $isPluginInitialized")
  }
   fun startSmsListenerService(context: Context) {
//    val serviceIntent = Intent(context, SmsListenerService::class.java)
////    serviceIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//    context.startService(serviceIntent)

    if (!isServiceRunning(context, SmsListenerService::class.java)) {
      val serviceIntent = Intent(context, SmsListenerService::class.java)
      Log.d("MyAlarmReceiver", "onReceive")
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        context.startForegroundService(serviceIntent)
      } else {
        Log.d("MyAlarmReceiver", "MyAlarmReceiver onReceive")
        context.startService(serviceIntent)
      }
    }

    Log.d("startSmsListenerService", "call startSmsListenerService-->")
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
  private fun tearDownPlugin() {
    IncomingSmsReceiver.foregroundSmsChannel = null
    smsChannel.setMethodCallHandler(null)

  }

}
