package com.sshtukin.alarmclock

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class BootCompletedIntentReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        if ("android.intent.action.BOOT_COMPLETED" == intent?.action) {
            val serviceIntent = Intent(context, SetAlarmJobService::class.java)
            SetAlarmJobService.enqueueWork(context, serviceIntent)
        }
    }
}