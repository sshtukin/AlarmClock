package com.sshtukin.alarmclock

import android.app.TimePickerDialog
import android.os.Bundle
import android.preference.PreferenceManager
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.text.DateFormat
import java.util.*

/**
 * Activity which contains [btnSetAlarm] to set alarm and
 *  [tvTime] which shows time of alarm
 *
 * @author Sergey Shtukin
 */

class MainActivity : AppCompatActivity(), TimePickerDialog.OnTimeSetListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnSetAlarm.setOnClickListener {
            val timePicker = TimePickerFragment()
            timePicker.show(supportFragmentManager, null)
        }
        updateTvTime(calendarFromPreferences(this))
    }

    override fun onTimeSet(view: TimePicker, hourOfDay: Int, minute: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
        calendar.set(Calendar.MINUTE, minute)
        calendar.set(Calendar.SECOND, 0)

        saveAlarmTime(calendar)
        updateTvTime(calendar)
        startAlarm(applicationContext, calendar)
    }

    private fun saveAlarmTime(calendar: Calendar) {
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(this)
        with(sharedPref.edit()) {
            putLong(PREFERENCES_KEY, calendar.timeInMillis)
            apply()
        }
    }

    private fun updateTvTime(calendar: Calendar?) {
        if (calendar != null){
            tvTime.text = DateFormat.getTimeInstance(DateFormat.SHORT).format(calendar.time)
        }
    }

    companion object {
        const val ALARM_REQUEST_CODE = 1001
        const val PREFERENCES_KEY = "alarm_key"
    }
}
