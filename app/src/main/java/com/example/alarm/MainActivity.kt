package com.example.alarm

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var materialTimePicker: MaterialTimePicker
    private lateinit var alarmManager: AlarmManager
    private lateinit var alarmClockInfo: AlarmManager.AlarmClockInfo
    private var setAlarm: Button? = null
    private val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
    private val calendar: Calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setAlarm = findViewById(R.id.alarm_button)
        setAlarm?.setOnClickListener {
            materialTimePicker = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(23)
                .setMinute(0)
                .setTitleText("Выберите время для будильника")
                .build()
            materialTimePicker.addOnPositiveButtonClickListener {
                calendar.set(Calendar.SECOND, 0)
                calendar.set(Calendar.MILLISECOND, 0)
                calendar.set(Calendar.MINUTE, materialTimePicker.minute)
                calendar.set(Calendar.HOUR_OF_DAY, materialTimePicker.hour)
                    alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
                    alarmClockInfo =
                    AlarmManager.AlarmClockInfo(
                        calendar.timeInMillis,
                        getAlarmInfoPendingIntent()
                    )
                alarmManager.setAlarmClock(alarmClockInfo, getAlarmActionPendingIntent())
                Toast.makeText(
                    this,
                    "Alarm is set at" + sdf.format(calendar.time),
                    Toast.LENGTH_SHORT
                ).show()
            }
            materialTimePicker.show(supportFragmentManager, "picker")
        }
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun getAlarmInfoPendingIntent(): PendingIntent? {
        val alarmInfoIntent = Intent(this, MainActivity::class.java)
        alarmInfoIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        return PendingIntent.getActivity(
            this,
            0,
            alarmInfoIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun getAlarmActionPendingIntent(): PendingIntent? {
        val intent = Intent(this, AlarmActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        return PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }
}