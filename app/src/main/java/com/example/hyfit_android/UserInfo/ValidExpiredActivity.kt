package com.example.hyfit_android.UserInfo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.hyfit_android.Login.LoginActivity

import com.example.hyfit_android.databinding.ActivityValidExpiredBinding
import com.example.hyfit_android.Login.LogoutActivity

class ValidExpiredActivity : AppCompatActivity() {
    lateinit var binding:ActivityValidExpiredBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityValidExpiredBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var logoutac=LogoutActivity()
        logoutac.logout(getJwt())
        val intent= Intent(this, LoginActivity::class.java)
        startActivity(intent)

    }
    private fun getJwt():String?{
        val spf = getSharedPreferences("auth", MODE_PRIVATE)
        return spf!!.getString("jwt","0")
    }
}