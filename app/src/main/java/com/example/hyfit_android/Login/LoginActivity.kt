package com.example.hyfit_android.Login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.hyfit_android.Join.JoinActivity1
import com.example.hyfit_android.MainActivity
import com.example.hyfit_android.UserRetrofitService
import com.example.hyfit_android.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity(), LoginView{

    lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var ljwt:String = intent.getStringExtra("logoutjwt").toString()
        Log.d("logoutjwt", ljwt)

        binding.join.setOnClickListener {
            val intent=Intent(this, JoinActivity1::class.java)
            startActivity(intent)
        }

        binding.loginNext.setOnClickListener{
            login()
//            val intent=Intent(this, MainActivity::class.java)
//            startActivity(intent)
        }
        binding.forgot.setOnClickListener {
            val intent=Intent(this, FindPasswordActivity1::class.java)
            startActivity(intent)
        }
//        bindingmain.goLogout.setOnClickListener {
//            logout(getJwt().toString())
//
//            val intent=Intent(this, LoginActivity::class.java)
//            startActivity(intent)
//        }

    }

    private fun login(){
        val email:String=binding.textEmail.text.toString()
        val password:String=binding.textPassword.text.toString()

        val usService = UserRetrofitService()
        usService.setLoginView(this)
        usService.login(LoginReq(email, password))
    }
    private fun saveJwt(jwt:String?){
        val spf=getSharedPreferences("auth", MODE_PRIVATE)
        val editor=spf.edit()
        editor.putString("jwt", jwt)
        editor.apply()
        editor.commit()
    }

    private fun getJwt():String?{
        val spf = getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)
        return spf!!.getString("jwt","0")
    }

//    fun deleteJwt(){
//        val spf=getSharedPreferences("auth", MODE_PRIVATE)
//        val editor=spf.edit()
//        editor.clear()
//        editor.apply()
//        editor.commit()
//    }

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
                if(code.toString()=="2003"){
                    Toast.makeText(this, "Incorrect password", Toast.LENGTH_LONG).show()
                }
                else if(code.toString()=="2002")
                {
                    Toast.makeText(this, "Email does not exist", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

//    public fun logout(jwt:String){
//        val usService=RetrofitService()
//        usService.setLogoutView(this)
//        usService.logout(jwt)
//    }


    override fun onLoginFailure(code: Int, msg: String) {
            Log.d("failure", code.toString())
            if(code.toString()=="2003"){
                Toast.makeText(this, "Incorrect password", Toast.LENGTH_LONG).show()
            }
            else if(code.toString()=="2002")
            {
                Toast.makeText(this, "Email does not exist", Toast.LENGTH_LONG).show()
            }
    }

//    override fun onLogoutSuccess(code: Int, msg: String) {
//        Log.d("logoutgoodgood", code.toString())
//    }
//
//    override fun onLogoutFailure(code: Int, msg: String) {
//        Log.d("lotoutsadsad", code.toString())
//    }


}