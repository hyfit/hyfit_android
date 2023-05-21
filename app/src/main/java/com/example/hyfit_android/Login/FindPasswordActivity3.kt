package com.example.hyfit_android.Login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.hyfit_android.UserRetrofitService
import com.example.hyfit_android.databinding.ActivityFindPassword3Binding

class FindPasswordActivity3 : AppCompatActivity(), FindPasswordView{ //FindPassword찾기
    lateinit var binding:ActivityFindPassword3Binding
    lateinit var email:String
    lateinit var password:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityFindPassword3Binding.inflate(layoutInflater)
        setContentView(binding.root)

        email=intent.getStringExtra("email").toString()

        binding.prev.setOnClickListener {
            val intent= Intent(this, FindPasswordActivity2::class.java)
            startActivity(intent)
        }

        binding.joinSubmit.setOnClickListener {
            password=binding.typeagain.text.toString()
            editpassword(email, password)
        }
    }

    private fun editpassword(email:String, password:String){
        val usService = UserRetrofitService()
        usService.setFindPasswordView(this)
        usService.editpassword(FindPasswordReq(email, password))
    }

    override fun onFindSuccess(code: Int, result: String) {
        Log.d("FindPasswordgoodGood", "goodgoodgood")
        val intent=Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    override fun onFindFailure(code: Int, msg: String) {
        Log.d("FindPasswordFailure", msg)
    }
}