package com.irfan.awesomeapp.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import java.util.HashMap

object BroadcastReceiverHelper {
    fun addObserver(
        context: Context?,
        notification: NotificationType,
        responseHandler: BroadcastReceiver?
    ) {
        LocalBroadcastManager.getInstance(context!!)
            .registerReceiver(responseHandler!!, IntentFilter(notification.name))
    }

    fun removeObserver(
        context: Context?,
        responseHandler: BroadcastReceiver?
    ) {
        LocalBroadcastManager.getInstance(context!!).unregisterReceiver(responseHandler!!)
    }

    fun postNotification(
        context: Context?,
        notification: NotificationType,
        params: HashMap<String, String>? = HashMap()
    ) {
        val intent = Intent(notification.name)
        // insert parameters if needed
        if (params != null) {
            for ((key, value) in params) {
                intent.putExtra(key, value)
            }
        }
        LocalBroadcastManager.getInstance(context!!).sendBroadcast(intent)
    }

    enum class NotificationType {
        NOCONNECTION
    }
}