package com.example.hyfit_android.Join

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.RadioGroup
import com.example.hyfit_android.databinding.ActivityJoin1Binding
import com.example.hyfit_android.databinding.ActivityJoin3Binding
import com.example.hyfit_android.databinding.ActivityJoin4Binding

class JoinActivity3 : AppCompatActivity() {
    lateinit var binding3 : ActivityJoin3Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding3= ActivityJoin3Binding.inflate(layoutInflater)
        setContentView(binding3.root)

        val password:String = intent.getStringExtra("password").toString()
        val email:String = intent.getStringExtra("email").toString()
        val name:String=intent.getStringExtra("name").toString()
        val phone:String=intent.getStringExtra("phone").toString()
        val birth:String=intent.getStringExtra("birth").toString()
        Log.d("hereherehere", phone)
        binding3.joinNext.setOnClickListener{
            //join()
            val intent = Intent(this, JoinActivity4::class.java )
            startActivity(intent)
        }
    }
    private fun join(){
        val nickName:String=binding3.textName.text.toString()

//        val usService = RetrofitService()
//        usService.join(JoinReq(name, email, password, nickName, phone, birth, gender))
    }
}