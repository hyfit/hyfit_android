package com.example.hyfit_android.Join

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.hyfit_android.databinding.ActivityJoin1Binding
import com.example.hyfit_android.databinding.ActivityJoin2Binding
import com.example.hyfit_android.databinding.ActivityJoin3Binding
import com.example.hyfit_android.databinding.ActivityJoin4Binding

class JoinActivity1 : AppCompatActivity() {
    lateinit var binding1 : ActivityJoin1Binding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding1=ActivityJoin1Binding.inflate(layoutInflater)
        setContentView(binding1.root)

        if(binding1.textPassword.text.toString().equals(binding1.PasswordAgain.text.toString())){
            binding1.correction.setText("Correct Password")
        }

        binding1.joinNext.setOnClickListener{
            val intent = Intent(this, JoinActivity2::class.java)
            intent.putExtra("email", binding1.textEmail.text.toString())
            intent.putExtra("password", binding1.textPassword.text.toString())

            startActivity(intent)
        }

    }

}