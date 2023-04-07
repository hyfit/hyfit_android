package com.example.hyfit_android.Join

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.hyfit_android.Login.LoginActivity
import com.example.hyfit_android.MainActivity
import com.example.hyfit_android.databinding.ActivityJoin4Binding
import com.example.hyfit_android.databinding.ActivityMainBinding

class JoinActivity4 : AppCompatActivity(){
    lateinit var binding4: ActivityJoin4Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding4= ActivityJoin4Binding.inflate(layoutInflater)
        setContentView(binding4.root)
//        var code1:String? = intent.getStringExtra("1").toString()
//        var code2:String? = intent.getStringExtra("2").toString()
//        var code3:String? = intent.getStringExtra("3").toString()
//        var code4:String? = intent.getStringExtra("4").toString()
//        Log.d("hihihi", code1+code2+code3+code4)


        binding4.joinStart.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}