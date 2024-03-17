package com.example.breathe

import android.app.AlarmManager
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import android.util.Log
import androidx.core.app.NotificationCompat


class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        // Build notification based on Intent
        Log.d("context", "$context")
        val notification: Notification = NotificationCompat.Builder(context, "breathe")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(intent.getStringExtra("title"))
            .setContentText(intent.getStringExtra("text"))
            .build()
        // Show notification
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(42, notification)
    }
}

fun createNotificationChannel(context: Context) {
    val name = "Breathe Entry"
    val desc = "Notifications about entry"
    val importance = NotificationManager.IMPORTANCE_DEFAULT
    val channel = NotificationChannel("breathe", name, importance)
    channel.description = desc
    val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    manager.createNotificationChannel(channel)
}

fun checkNotifications(context: Context) {
    val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    if (manager.areNotificationsEnabled()) {

    }
}

fun scheduleNotification(context: Context, interval: Long, title: String?, text: String?) {
    val intent = Intent(context, NotificationReceiver::class.java)
    intent.putExtra("title", title)
    intent.putExtra("text", text)
    val pending = PendingIntent.getBroadcast(context, 42, intent, PendingIntent.FLAG_IMMUTABLE)
    // Schdedule notification
    val manager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    manager.setInexactRepeating(
        AlarmManager.ELAPSED_REALTIME,
        SystemClock.elapsedRealtime() + interval,
        interval,
        pending
    )
}

fun cancelNotification(context: Context, title: String?, text: String?) {
    val intent = Intent(context, NotificationReceiver::class.java)
    intent.putExtra("title", title)
    intent.putExtra("text", text)
    val pending = PendingIntent.getBroadcast(context, 42, intent, PendingIntent.FLAG_IMMUTABLE)
    // Cancel notification
    val manager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    manager.cancel(pending)
}
