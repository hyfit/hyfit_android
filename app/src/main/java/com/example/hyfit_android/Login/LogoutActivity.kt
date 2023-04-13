package com.example.hyfit_android.Login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.hyfit_android.UserRetrofitService
import com.example.hyfit_android.databinding.ActivityLogoutBinding

class LogoutActivity : AppCompatActivity(), LogoutView {
    lateinit var binding:ActivityLogoutBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityLogoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d("realjwt", getJwt().toString())

        binding.yes.setOnClickListener {
            logout(getJwt().toString())
            deleteJwt()
            val intent= Intent(this, LoginActivity::class.java)
            intent.putExtra("logoutjwt", getJwt().toString())
            startActivity(intent)
        }
    }
    fun logout(jwt:String?){
        val usService= UserRetrofitService()
        usService.setLogoutView(this)
        usService.logout(jwt)
    }

    private fun deleteJwt(){
        val spf=getSharedPreferences("auth", MODE_PRIVATE)
        val editor=spf.edit()
        editor.clear()
        editor.apply()
        editor.commit()
    }

    private fun saveJwt(jwt:String?){
        val spf=getSharedPreferences("auth", MODE_PRIVATE)
        val editor=spf.edit()
        editor.putString("jwt", jwt)
        editor.apply()
        editor.commit()
    }
    private fun getJwt():String?{
        val spf = getSharedPreferences("auth", MODE_PRIVATE)
        return spf!!.getString("jwt","0")
    }

    override fun onLogoutSuccess(code: Int, msg: String) {
        Log.d("logoutgoodgood", code.toString())
    }

    override fun onLogoutFailure(code: Int, msg: String) {
        Log.d("lotoutsadsad", code.toString())
    }
}