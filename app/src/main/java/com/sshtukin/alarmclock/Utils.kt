package com.sshtukin.alarmclock

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.preference.PreferenceManager
import java.util.*


fun startAlarm(context: Context, calendar: Calendar?) {
    if (calendar != null) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlertReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, MainActivity.ALARM_REQUEST_CODE, intent, 0)
        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis - 5 * MINUTE,
            DAY,
            pendingIntent
        )
    }
}

fun calendarFromPreferences(context: Context): Calendar? {
    val calendar = Calendar.getInstance()
    val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
    val time = sharedPref.getLong(MainActivity.PREFERENCES_KEY, 0)
    return if (time != 0L) {
        calendar.timeInMillis = time
        calendar
    } else null
}

const val MINUTE = 1000 * 60L
const val DAY = 24 * 60 * 60 * 1000L