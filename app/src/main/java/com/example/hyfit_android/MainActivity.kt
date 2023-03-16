package com.example.hyfit_android


import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.hyfit_android.databinding.ActivityMainBinding
import com.example.hyfit_android.pinnacle.PinnacleActivity
import java.io.IOException
import java.util.*
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    val PERMISSIONS_REQUEST_LOCATION = 1000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // permission code
        if ((ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED) ||
            (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACTIVITY_RECOGNITION
            ) != PackageManager.PERMISSION_GRANTED)
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACTIVITY_RECOGNITION),
                PERMISSIONS_REQUEST_LOCATION
            )
            return
        }
    }

    // permission code
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode === PERMISSIONS_REQUEST_LOCATION) {
            if (grantResults.size > 0) {
                for (grant in grantResults) {
                    if (grant != PackageManager.PERMISSION_GRANTED) System.exit(0)
                    else {
                        binding.MoveButton.setOnClickListener{
                            startActivity(Intent(this@MainActivity, PinnacleActivity::class.java))
                        }
                    }
                }
            }
        }
    }
}

