package com.example.mycompanion

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class GoalActivity : AppCompatActivity() {

    private lateinit var goalInput: EditText
    private lateinit var startTrainingButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_goal)

        goalInput = findViewById(R.id.goalInput)
        startTrainingButton = findViewById(R.id.startTrainingButton)

        startTrainingButton.setOnClickListener {
            val goal = goalInput.text.toString()
            Log.d("GoalActivity", "Goal for today: $goal")

            // Start TimerActivity
            val intent = Intent(this, TimerActivity::class.java)
            startActivity(intent)
        }
    }
}
