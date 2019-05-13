package com.sshtukin.alarmclock

import android.content.Context
import android.content.Intent
import androidx.core.app.JobIntentService


class SetAlarmJobService : JobIntentService() {

    override fun onHandleWork(intent: Intent) {
        startAlarm(this, calendarFromPreferences(this))
    }

    companion object {
        private const val jobId = 1004
        fun enqueueWork(context: Context, work: Intent) {
            enqueueWork(context, SetAlarmJobService::class.java, jobId, work)
        }
    }
}