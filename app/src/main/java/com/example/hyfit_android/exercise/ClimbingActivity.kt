package com.example.hyfit_android.exercise

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.hyfit_android.databinding.ActivityClimbingBinding

class ClimbingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityClimbingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding  = ActivityClimbingBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

    }
}