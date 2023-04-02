package com.example.hyfit_android.Login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.hyfit_android.Join.JoinEmailActivity
import com.example.hyfit_android.Join.JoinEmailView
import com.example.hyfit_android.UserRetrofitService
import com.example.hyfit_android.databinding.ActivityFindPasswordBinding

class FindPasswordActivity1 : AppCompatActivity(), JoinEmailView {
    lateinit var binding: ActivityFindPasswordBinding
    lateinit var email:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityFindPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.joinSubmit.setOnClickListener{
            email=binding.typehere.text.toString()
            Log.d("emailhere", email)
            confirm(email)
        }

        binding.prev.setOnClickListener {
            val intent=Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun confirm(email:String?){
        //var email:String = intent.getStringExtra("email").toString()
        val usService = UserRetrofitService()
        usService.setJoinEmailView(this)
        usService.confirm(email)
    }

    override fun onEmailSuccess(code: Int, result: String) {
        val intent= Intent(this, FindPasswordActivity2::class.java)
        val spf=getSharedPreferences("passmail", MODE_PRIVATE)
        val editor=spf.edit()
        editor.putString("passcode", result)
        editor.apply()
        editor.commit()
        startActivity(intent)
    }

    override fun onEmailFailure(code: Int, msg: String) {
        Log.d("PasswordEmailFailure", "sadsad")
    }
}