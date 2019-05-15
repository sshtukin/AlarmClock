package com.sshtukin.alarmclock

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build

/**
 * BroadcastReceiver which runs AlarmClockService
 *
 * @author Sergey Shtukin
 */

class AlertReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        startAlarmService(context)
    }
}