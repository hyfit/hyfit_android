package com.example.hyfit_android.Login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.hyfit_android.R
import com.example.hyfit_android.databinding.ActivityFindPassword3Binding

class FindPasswordActivity3 : AppCompatActivity() {
    lateinit var binding:ActivityFindPassword3Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityFindPassword3Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.prev.setOnClickListener {
            val intent= Intent(this, FindPasswordActivity2::class.java)
            startActivity(intent)
        }
    }
}