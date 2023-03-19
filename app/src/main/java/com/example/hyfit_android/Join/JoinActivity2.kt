package com.example.hyfit_android.Join

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.hyfit_android.databinding.ActivityJoin1Binding
import com.example.hyfit_android.databinding.ActivityJoin2Binding
import com.example.hyfit_android.databinding.ActivityJoin3Binding
import com.example.hyfit_android.databinding.ActivityJoin4Binding

class JoinActivity2 : AppCompatActivity() {


    lateinit var binding2 : ActivityJoin2Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding2= ActivityJoin2Binding.inflate(layoutInflater)
        setContentView(binding2.root)

        var password:String = intent.getStringExtra("password").toString()
        var email:String = intent.getStringExtra("email").toString()
        Log.d("hereherehere", email)
        binding2.joinNext.setOnClickListener{
            val intent2 = Intent(this, JoinActivity3::class.java )
            intent2.putExtra("name", binding2.textName.text.toString())
            intent2.putExtra("birth", binding2.textBirth.text.toString())
            intent2.putExtra("phone", binding2.textPhoneNumber.text.toString())
            intent2.putExtra("email", email)
            intent2.putExtra("password", password)
            startActivity(intent2)
        }
    }
}