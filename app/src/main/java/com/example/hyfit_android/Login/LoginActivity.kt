package com.example.hyfit_android.Login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.hyfit_android.Join.JoinActivity1
import com.example.hyfit_android.MainActivity
import com.example.hyfit_android.RetrofitService
import com.example.hyfit_android.databinding.ActivityJoin4Binding
import com.example.hyfit_android.databinding.ActivityLoginBinding
import com.example.hyfit_android.databinding.ActivityMainBinding

class LoginActivity : AppCompatActivity(), LoginView {

    lateinit var binding: ActivityLoginBinding
    lateinit var bindingmain:ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityLoginBinding.inflate(layoutInflater)
        bindingmain=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.join.setOnClickListener {
            val intent=Intent(this, JoinActivity1::class.java)
            startActivity(intent)
        }

        binding.loginNext.setOnClickListener{
            login()
//            val intent=Intent(this, MainActivity::class.java)
//            startActivity(intent)
        }
    }

    private fun login(){
        val email:String=binding.textEmail.text.toString()
        val password:String=binding.textPassword.text.toString()

        val usService = RetrofitService()
        usService.setLoginView(this)
        usService.login(LoginReq(email, password))
    }
    private fun saveJwt(jwt:String){
        val spf=getSharedPreferences("auth", MODE_PRIVATE)
        val editor=spf.edit()
        editor.putString("jwt", jwt)
        editor.apply()
    }

    override fun onLoginSuccess(code: Int, jwt: String) {
        when(code){
            1000->{
                Log.d("Success", code.toString())
                saveJwt(jwt)
                val intent=Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            else->{
                Log.d("error", code.toString())
            }
        }
    }

    override fun onLoginFailure(code: Int, msg: String) {
            Log.d("failure", code.toString())
    }
}