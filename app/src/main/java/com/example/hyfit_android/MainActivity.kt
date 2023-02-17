package com.example.hyfit_android


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.hyfit_android.databinding.ActivityMainBinding
import com.example.hyfit_android.pinnacle.PinnacleActivity

class MainActivity : AppCompatActivity() {
    lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.CalcButton.setOnClickListener{
            startActivity(Intent(this@MainActivity,PinnacleActivity::class.java))
        }
    }
}