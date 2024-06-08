package com.example.traininganalyst

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var selectedSport: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onSportClick(view: View) {
        val sportLayout = view as LinearLayout
        selectedSport = sportLayout.tag.toString()
        Log.d("MainActivity", "Selected Sport: $selectedSport")
        // You can use the selectedSport variable later in your code
    }
}
