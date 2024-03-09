package com.example.googleassistant.assistant

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.googleassistant.MainActivity
import com.example.googleassistant.R
import com.example.googleassistant.data.Weather
import com.example.googleassistant.data.WeatherApiService
import com.example.googleassistant.databinding.ActivityExploreBinding
import com.example.googleassistant.utils.Utils.Commands
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.chip.Chip
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Calendar

class ExploreActivity : AppCompatActivity() {
     private lateinit var binding:ActivityExploreBinding
     var latitude:String?  = null
     var longitude:String?= null
     lateinit var weather_url1:String
     private lateinit var API_KEY : String
     private lateinit var fusedLocationClient: FusedLocationProviderClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExploreBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        API_KEY = "c7346b27cf654f499c6cf75729fe525d"
        weather_url1 = "https://api.weatherbit.io/v2.0/"
        checkPermission()
        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

            val builder = AlertDialog.Builder(this)

            builder.setTitle("Turn on \"Location\"")

            builder.setMessage("\"Location\" is currently turned off. Turn on \"Location\" to enable navigation function in GoogleAssistant")

            builder.setPositiveButton("OK") { _, _ ->
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
            dialog.window?.setBackgroundDrawableResource(android.R.color.darker_gray)
            // Display the alert dialog on app interface
            dialog.show()
        }

        for (command in Commands) {
            val chip = Chip(this)
            chip.text = command.toString()
            chip.setTextAppearance(R.style.chips)
            chip.setButtonDrawable(R.drawable.shape)
            chip.setPadding(25, 10, 25, 10)
            binding.chipsCommand.addView(chip)
        }
        binding.weatherCardView.setOnClickListener {
            if(latitude == null && longitude==null){
                checkPermission()
            }
            else{
                obtainLocation()
            }

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

    private fun checkPermission(){
        val permissions = arrayOf(
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        )
        if (checkSelfPermission(permissions[0]) != PackageManager.PERMISSION_GRANTED &&
            checkSelfPermission(permissions[1])!=PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, permissions, 1)
        }else{
            fusedLocationClient.lastLocation.addOnSuccessListener {
                if (it != null)
                {
                    handleLocation(it)
                }
                else{
                    Toast.makeText(this, "Location is unavailable try after some time", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun handleLocation(location: Location) {
        if (location!= null) {
            latitude = location.latitude.toString()
            longitude = location.longitude.toString()
            obtainLocation()
        } else {
            Toast.makeText(this, "Location is unavailable try after some time", Toast.LENGTH_SHORT).show()
        }
    }
    private fun obtainLocation() {
        val lati = latitude
        val long = longitude
        if (long != null && lati != null) {
            val api = Retrofit.Builder()
            .baseUrl(weather_url1)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherApiService::class.java)
            api.getWeatherData(lati,long,API_KEY,"minutely").enqueue(object:Callback<Weather>{
                override fun onResponse(call: Call<Weather>, response: Response<Weather>) {
                    if (response.isSuccessful){
                        val body =  response.body()
                        if (body != null){
                            val data = body.data
                            val cityname = data[0].city_name
                            val temp = data[0].temp.toString()
                            Log.e("puneet", cityname)
                            binding.textView.text = " ${temp}°C $cityname "
                            binding.wind.text = data[0].weather.description
                            binding.today.text = data[0].datetime
                        }
                        else{
                            Toast.makeText(this@ExploreActivity, "Error in api", Toast.LENGTH_SHORT).show()
                        }
                    }else{
                        Toast.makeText(this@ExploreActivity, "Limit is exceeded try after some time", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Weather>, t: Throwable) {
                    Log.e("puneet", "$t.message")
                }

            })
        }else{
           checkPermission()
            Toast.makeText(this@ExploreActivity, "lat and long are not initialised", Toast.LENGTH_SHORT).show()
        }

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