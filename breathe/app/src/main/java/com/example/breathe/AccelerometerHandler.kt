package com.example.breathe

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.VibrationEffect
import android.os.Vibrator

class AccelerometerHandler(ctx: Context) {
    private val sManager = ctx.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val vibrator = ctx.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    private lateinit var sListener : SensorEventListener

    var data: Float = 0.0F
        private set

    fun register() {
        val sensor = sManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        sListener = object : SensorEventListener {
            override fun onSensorChanged(sEvent: SensorEvent?) {
                val value = sEvent?.values  // Ускорения по осям XYZ
                if (value != null) {
                    data = value[2]       // Ось Z
                }
            }

            override fun onAccuracyChanged(p0: Sensor?, p1: Int) {}
        }
        sManager.registerListener(sListener, sensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    fun unregister() {
        if (this::sListener.isInitialized) {
            sManager.unregisterListener(sListener)
        }
    }

    fun vibrate() {
        val effect = VibrationEffect.createOneShot(
            500, VibrationEffect.DEFAULT_AMPLITUDE
        )
        vibrator.vibrate(effect)
    }
}