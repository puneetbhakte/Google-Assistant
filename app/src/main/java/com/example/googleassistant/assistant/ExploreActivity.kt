package com.example.googleassistant.assistant

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.googleassistant.MainActivity
import com.example.googleassistant.R
import com.example.googleassistant.databinding.ActivityExploreBinding
import com.example.googleassistant.utils.Utils.Commands
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.chip.Chip
import org.json.JSONObject
import java.util.Calendar

class ExploreActivity : AppCompatActivity() {
    private lateinit var binding:ActivityExploreBinding

    var weather_url1 = "https://api.weatherbit.io/v2.0/current?lat=23.2836915&lon=77.4617912&key=bf39b322ecae4ebc82b09d6ef5849795"
    //api id for url
    var api_id1 = "bf39b322ecae4ebc82b09d6ef5849795"

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExploreBinding.inflate(layoutInflater)
        setContentView(binding.root)


        if (ContextCompat.checkSelfPermission(this@ExploreActivity,
                Manifest.permission.ACCESS_FINE_LOCATION) !==
            PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this@ExploreActivity,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this@ExploreActivity,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
            } else {
                ActivityCompat.requestPermissions(this@ExploreActivity,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
            }
        }


        for (command in Commands) {
            val chip = Chip(this)
            chip.text = command.toString()
            chip.setTextAppearance(R.style.chips)
            chip.setButtonDrawable(R.drawable.shape)
            chip.setPadding(25, 10, 25, 10)
            binding.chipsCommand.addView(chip)
        }

        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

            // Alert user to switch Location on in Settings
            val builder = AlertDialog.Builder(this)


            // Set the alert dialog title
            builder.setTitle("Turn on \"Location\"")

            // Display a message on alert dialog
            builder.setMessage("\"Location\" is currently turned off. Turn on \"Location\" to enable navigation function in GoogleAssistant")

            // Set a positive button and its click listener on alert dialog
            builder.setPositiveButton("OK") { dialog, which ->

                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }

            // Display a negative button on alert dialog
            builder.setNegativeButton("Cancel") { dialog, which ->
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)

            }

            // Finally, make the alert dialog using builder
            val dialog: AlertDialog = builder.create()
            dialog.window?.setBackgroundDrawableResource(android.R.color.darker_gray);
            // Display the alert dialog on app interface
            dialog.show()
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
         Log.e("lat", weather_url1)
        obtainLocation()
             binding.weatherCardView.setOnClickListener {
            obtainLocation()
        }
        val c: Calendar = Calendar.getInstance()
        when (c.get(Calendar.HOUR_OF_DAY)) {
            in 0..11 -> {
                binding.greetings.text = "Good Morning !"
            }
            in 12..15 -> {
                binding.greetings.text = "Good Afternoon !"
            }
            in 16..20 -> {
                binding.greetings.text = "Good Evening !"
            }
            in 21..23 -> {
                binding.greetings.text = "Good Night !"
            }
        }

    }

    private fun obtainLocation() {
        Log.e("lat", "function")
        Log.e("lat", weather_url1.toString())
        //this function will fetch data from URL
        // Instantiate the RequestQueue.
        val queue = Volley.newRequestQueue(this)
        Log.e("lat", "function0")
        Log.e("lat", weather_url1)
        // Request a string response from the provided URL.
        val stringReq = StringRequest(
            Request.Method.GET, weather_url1,
            { response ->
                Log.e("lat", response.toString())
                //get the JSON object
                val obj = JSONObject(response)
                //get the Array from obj of name - "data"
                val arr = obj.getJSONArray("data")
                Log.e("lat obj1", arr.toString())
                //get the JSON object from the array at index position 0
                val obj2 = arr.getJSONObject(0)
                Log.e("lat obj2", obj2.toString())
                //set the temperature and the city name using getString() function
                binding.textView.text = obj2.getString("temp") + "°C " + obj2.getString("city_name")
                binding.today.text = "TODAY | " + obj2.getString("datetime")
                binding.wind.text = obj2.getJSONObject("weather").getString("description")
            },
            //In case of any error
            { Log.e("error", "not worked") })
        queue.add(stringReq)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1 -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED) {
                    if ((ContextCompat.checkSelfPermission(this@ExploreActivity,
                            Manifest.permission.ACCESS_FINE_LOCATION) ==
                                PackageManager.PERMISSION_GRANTED)) {
                        Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
                }
                return
            }
        }
    }

}