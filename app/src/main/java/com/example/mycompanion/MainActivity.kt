package com.example.mycompanion

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.JustifyContent


class MainActivity : AppCompatActivity() {

    private lateinit var selectedSport: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val suffix = "_emoji"
        val filteredResources = getResourcesWithSuffix(suffix)

        for (resource in filteredResources) {
            Log.d("MainActivity", "Resource: $resource")
        }
        // Define the list of sports
        val sports = listOf(
            Sport(getString(R.string.badminton), getString(R.string.badminton_emoji), getString(R.string.badminton)),
            Sport(getString(R.string.basketball), getString(R.string.basketball_emoji), getString(R.string.basketball)),
            Sport(getString(R.string.climbing), getString(R.string.climbing_emoji), getString(R.string.climbing)),
            Sport(getString(R.string.hockey), getString(R.string.hockey_emoji), getString(R.string.hockey)),
            Sport(getString(R.string.trail), getString(R.string.trail_emoji), getString(R.string.trail)),
            Sport(getString(R.string.running), getString(R.string.running_emoji), getString(R.string.running)),
            Sport(getString(R.string.biking), getString(R.string.biking_emoji), getString(R.string.biking))
            // Add more sports as needed
        )

        // Initialize the RecyclerView with FlexboxLayoutManager
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        val layoutManager = FlexboxLayoutManager(this).apply {
            flexDirection = FlexDirection.ROW
            justifyContent = JustifyContent.FLEX_START
        }
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = SportAdapter(sports) { sport -> onSportClick(sport) }
    }

    private fun onSportClick(sport: Sport) {
        selectedSport = sport.name
        Log.d("MainActivity", "Selected Sport: $selectedSport")

        // Start GoalActivity
        val intent = Intent(this, GoalActivity::class.java).apply {
            putExtra("SELECTED_SPORT", selectedSport)
        }
        startActivity(intent)
    }
    private fun getResourcesWithSuffix(suffix: String): List<String> {
        val resources = mutableListOf<String>()
        val fields = R.drawable::class.java.fields // You can change this to R.string, R.layout, etc.

        for (field in fields) {
            if (field.name.endsWith(suffix)) {
                resources.add(field.name)
            }
        }
        return resources
    }
}
