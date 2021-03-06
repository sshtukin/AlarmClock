package com.sshtukin.alarmclock

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import java.util.*

/**
 * Foreground service which shows notification, until set time,
 * than plays ringtone and shows toast
 *
 * @author Sergey Shtukin
 */

class AlarmClockService : Service() {
    private lateinit var notificationManager: NotificationManager
    private lateinit var notificationBuilder: NotificationCompat.Builder
    private var calendar: Calendar? = null

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (ACTION_STOP_SERVICE == intent?.action) {
            stopAlarmClockService()
        } else {
            running = true
            notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationBuilder = getNotificationBuilder()
            calendar = calendarFromPreferences(this)
            val timeLeft = calendar!!.timeInMillis - Calendar.getInstance().timeInMillis
            notificationBuilder.setContentText((timeLeft / MINUTE + 1).toString() + " minute(s)")
            startForeground(FOREGROUND_ID, notificationBuilder.build())
            doBackground()
        }
        return START_STICKY
    }

    private fun doBackground() {
        Thread(Runnable {
            while (running && (Calendar.getInstance().timeInMillis <= calendar!!.timeInMillis)) {
                val timeLeft = calendar!!.timeInMillis - Calendar.getInstance().timeInMillis
                notificationBuilder
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setContentText((timeLeft / MINUTE + 1).toString() + " minute(s)")
                notificationManager.notify(FOREGROUND_ID, notificationBuilder.build())
                if (timeLeft > MINUTE) {
                    Thread.sleep(MINUTE)
                } else {
                    Thread.sleep(timeLeft)
                }
            }
            ring()
            stopAlarmClockService()
        }).start()
    }

    private fun ring() {
        val mainHandler = Handler(mainLooper)
        mainHandler.post {
            Toast.makeText(
                applicationContext,
                getString(R.string.ring),
                Toast.LENGTH_SHORT
            ).show()
        }

        val player = MediaPlayer.create(this, R.raw.song)
        player.start()
    }

    private fun stopAlarmClockService() {
        stopForeground(true)
        stopSelf()
    }

    private fun getNotificationBuilder(): NotificationCompat.Builder {
        val stopSelfIntent = Intent(this, AlarmClockService::class.java).setAction(ACTION_STOP_SERVICE)
        val stopSelfPendingIntent = PendingIntent.getService(this, 0, stopSelfIntent, 0)
        val channelId = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(
                getString(R.string.for_service),
                getString(R.string.for_service)
            )
        } else {
            ""
        }
        return NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle(getString(R.string.alarm_will_ring))
            .addAction(
                R.drawable.ic_launcher_background, getString(R.string.cancel),
                stopSelfPendingIntent
            )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(channelId: String, channelName: String): String {
        val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_NONE)
        with(getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager) {
            createNotificationChannel(channel)
        }
        return channelId
    }

    override fun onDestroy() {
        super.onDestroy()
        running = false
    }

    companion object {
        const val ACTION_STOP_SERVICE = "STOP_SERVICE"
        const val FOREGROUND_ID = 4242
        private var running = true
    }
}