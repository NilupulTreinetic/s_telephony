//package com.shounakmulay.telephony
//
//import android.content.Intent
//import android.widget.Toast
//import com.shounakmulay.telephony.sms.ContextHolder.applicationContext
//
//import io.flutter.embedding.engine.plugins.FlutterPlugin
//import io.flutter.plugin.common.MethodCall
//import io.flutter.plugin.common.MethodChannel
//
//class SmsServicePlugin : FlutterPlugin {
//    private var channel: MethodChannel? = null
//
//    override fun onAttachedToEngine(binding: FlutterPlugin.FlutterPluginBinding) {
//        channel = MethodChannel(binding.binaryMessenger, "SMS_SERVICE")
//        channel?.setMethodCallHandler { call, result ->
//            when (call.method) {
//                "startSmsService" -> {
//                    startSmsService()
//                    result.success(null)
//                }
//                else -> {
//                    result.notImplemented()
//                }
//            }
//        }
//    }
//
//    override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
//        channel?.setMethodCallHandler(null)
//    }
//
//    private fun startSmsService() {
//
//        // Your SMS service initialization and logic go here
//        // Example: Start a background service or register a BroadcastReceiver for SMS events
//        applicationContext?.let { context ->
//            // Create an Intent to start your SMS listening service
////            val serviceIntent = Intent(context, SmsListenerService::class.java)
////            // Start the service
////            context.startService(serviceIntent)
//            Toast.makeText(
//                context,
//                "Start recieved SMS service",
//                Toast.LENGTH_LONG
//            ).show()
//        }
//    }
//}