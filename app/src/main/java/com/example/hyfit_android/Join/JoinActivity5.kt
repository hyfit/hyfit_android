package com.example.hyfit_android.Join

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.hyfit_android.Login.LoginActivity
import com.example.hyfit_android.databinding.ActivityJoin4Binding

class JoinActivity5 : AppCompatActivity(){
    lateinit var binding4: ActivityJoin4Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding4= ActivityJoin4Binding.inflate(layoutInflater)
        setContentView(binding4.root)


        binding4.joinStart.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}