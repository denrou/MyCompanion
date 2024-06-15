package com.example.mycompanion

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.GnssStatus
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.JustifyContent

class MainActivity : AppCompatActivity(), LocationListener {

    private lateinit var selectedSport: String
    private lateinit var locationManager: LocationManager
    private lateinit var gpsStatusCircle: View
    private var gpsSignalAcquired = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        gpsStatusCircle = findViewById(R.id.gps_status_circle)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
        } else {
            requestLocationUpdates()
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

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                requestLocationUpdates()
            } else {
                // Permission denied, show a message to the user
                Log.d("MainActivity", "Permission denied")
            }
        }
    }

    private fun requestLocationUpdates() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            gpsStatusCircle.visibility = View.VISIBLE
            gpsStatusCircle.setBackgroundResource(R.drawable.yellow_circle)
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, this)
            locationManager.registerGnssStatusCallback(gnssStatusCallback, null)
        }
    }

    private val gnssStatusCallback = object : GnssStatus.Callback() {
        override fun onStarted() {
            gpsStatusCircle.visibility = View.VISIBLE
            gpsStatusCircle.setBackgroundResource(R.drawable.yellow_circle)
            gpsSignalAcquired = false
            Log.d("MainActivity", "GPS searching for signal")
        }

        override fun onStopped() {
            gpsStatusCircle.visibility = View.GONE
            gpsSignalAcquired = false
            Log.d("MainActivity", "GPS stopped")
        }

        override fun onFirstFix(ttffMillis: Int) {
            gpsStatusCircle.setBackgroundResource(R.drawable.green_circle)
            gpsSignalAcquired = true
            Log.d("MainActivity", "GPS signal acquired")
        }

        override fun onSatelliteStatusChanged(status: GnssStatus) {
            if (gpsSignalAcquired) {
                gpsStatusCircle.setBackgroundResource(R.drawable.green_circle)
            }
        }
    }

    override fun onLocationChanged(location: Location) {
        Log.d("MainActivity", "Location: ${location.latitude}, ${location.longitude}")
        // GPS signal is acquired, setting the circle to green is handled by onFirstFix
        gpsStatusCircle.setBackgroundResource(R.drawable.green_circle)
        gpsSignalAcquired = true
    }

    override fun onProviderEnabled(provider: String) {
        gpsStatusCircle.visibility = View.VISIBLE
        gpsStatusCircle.setBackgroundResource(R.drawable.yellow_circle)
        Log.d("MainActivity", "GPS provider enabled")
    }

    override fun onProviderDisabled(provider: String) {
        gpsStatusCircle.visibility = View.GONE
        gpsSignalAcquired = false
        Log.d("MainActivity", "GPS provider disabled")
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

}
