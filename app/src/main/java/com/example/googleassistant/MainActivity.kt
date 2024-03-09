package com.example.googleassistant


import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.googleassistant.assistant.AssistantActivity
import com.example.googleassistant.assistant.ExploreActivity
import com.example.googleassistant.databinding.ActivityMainBinding
import com.example.googleassistant.functions.GoogleLensActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar


class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    private val Record_Audio_Request_Code:Int=1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)



        val calendar = Calendar.getInstance()
        val formattedDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.time)
        val splitDate = formattedDate.split(",").toTypedArray()
        val date = splitDate[1].trim { it <= ' ' }

        val format = SimpleDateFormat("HH:mm:ss")
        val time = format.format(calendar.time)

        binding.dateTime.text = "Data : $date  Time : $time"

        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.RECORD_AUDIO) != PERMISSION_GRANTED) {
            checkPermission()
        }
        binding.actionButton.setOnClickListener {
            startActivity(Intent(this, AssistantActivity::class.java))
        }
        binding.hiGoogle.setOnClickListener {
            startActivity(Intent(this, AssistantActivity::class.java))
        }
        binding.actionGoogleLens.setOnClickListener {
            startActivity(Intent(this, GoogleLensActivity::class.java))
        }
        binding.actionExplore.setOnClickListener {
            startActivity(Intent(this, ExploreActivity::class.java))
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode==Record_Audio_Request_Code && grantResults.isNotEmpty())
        {
            //permigranted type of array that stores all the permission
            if (grantResults[0]== PERMISSION_GRANTED)
            {
                Toast.makeText(this,"Permission Granted",Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun checkPermission(){
        ActivityCompat.requestPermissions(this,
            arrayOf(android.Manifest.permission.RECORD_AUDIO),
            Record_Audio_Request_Code )
    }

    }


