package com.example.mycompanion

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale

class TimerActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var timerTextView: TextView
    private lateinit var stepsTextView: TextView
    private lateinit var pauseButton: Button
    private lateinit var stopButton: Button
    private var handler = Handler(Looper.getMainLooper())
    private var startTime = 0L
    private var timeInSeconds = 0L
    private var running = false

    private var sensorManager: SensorManager? = null
    private var stepSensor: Sensor? = null
    private var totalSteps = 0f
    private var previousTotalSteps = 0f
    private val stepTimestamps = mutableListOf<Long>()

    private val runnable: Runnable = object : Runnable {
        override fun run() {
            if (running) {
                val millis = System.currentTimeMillis() - startTime
                timeInSeconds = millis / 1000
                val hours = timeInSeconds / 3600
                val minutes = (timeInSeconds % 3600) / 60
                val seconds = timeInSeconds % 60
                timerTextView.text = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds)
                handler.postDelayed(this, 1000)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer)

        timerTextView = findViewById(R.id.timerTextView)
        stepsTextView = findViewById(R.id.stepsTextView)
        pauseButton = findViewById(R.id.pauseButton)
        stopButton = findViewById(R.id.stopButton)

        startTime = System.currentTimeMillis()
        running = true
        handler.post(runnable)

        pauseButton.setOnClickListener {
            running = !running
            if (running) {
                startTime = System.currentTimeMillis() - timeInSeconds * 1000
                handler.post(runnable)
                pauseButton.text = getString(R.string.pause)
            } else {
                pauseButton.text = getString(R.string.resume)
            }
        }

        stopButton.setOnClickListener {
            Log.d("TimerActivity", "Training stopped at ${timerTextView.text}")
            Log.d("TimerActivity", "Step Timestamps: $stepTimestamps")
            finishAffinity()
        }

        setupStepSensor()
    }

    private fun setupStepSensor() {
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        stepSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        if (stepSensor != null) {
            Log.d("TimerActivity", "Step sensor available, registering listener.")
            sensorManager?.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI)
        } else {
            Log.e("TimerActivity", "Step sensor not available!")
        }
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_STEP_COUNTER) {
            if (previousTotalSteps == 0f) {
                previousTotalSteps = event.values[0]
            }

            val currentSteps = event.values[0] - previousTotalSteps
            totalSteps = currentSteps
            stepsTextView.text = String.format(Locale.getDefault(), "Steps: %d", totalSteps.toInt())
            stepTimestamps.add(System.currentTimeMillis())
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Not used
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("TimerActivity", "Step Timestamps on Destroy: $stepTimestamps")
        sensorManager?.unregisterListener(this)
    }
}
