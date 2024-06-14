package com.example.traininganalyst

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class TimerActivity : AppCompatActivity() {

    private lateinit var timerTextView: TextView
    private lateinit var pauseButton: Button
    private lateinit var stopButton: Button
    private var handler = Handler()
    private var startTime = 0L
    private var timeInSeconds = 0L
    private var running = false

    private val runnable: Runnable = object : Runnable {
        override fun run() {
            if (running) {
                val millis = System.currentTimeMillis() - startTime
                timeInSeconds = millis / 1000
                val hours = timeInSeconds / 3600
                val minutes = (timeInSeconds % 3600) / 60
                val seconds = timeInSeconds % 60
                timerTextView.text = String.format("%02d:%02d:%02d", hours, minutes, seconds)
                handler.postDelayed(this, 1000)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer)

        timerTextView = findViewById(R.id.timerTextView)
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
                pauseButton.text = "Pause"
            } else {
                pauseButton.text = "Resume"
            }
        }

        stopButton.setOnClickListener {
            Log.d("TimerActivity", "Training stopped at ${timerTextView.text}")
            finishAffinity()
        }
    }
}
