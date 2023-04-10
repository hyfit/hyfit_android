package com.example.hyfit_android.Join

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.hyfit_android.Login.LoginActivity
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

        binding1.prev.setOnClickListener {
            val intent=Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        binding1.joinNext.setOnClickListener{
            if (binding1.textEmail.text.toString().isEmpty() || binding1.textPassword.text.toString().isEmpty() || binding1.PasswordAgain.text.toString().isEmpty()){
                Toast.makeText(this,"Fill in all the blanks", Toast.LENGTH_LONG).show()
                Log.d("test", "fill in all the blanks")
            }
            else if(!binding1.textPassword.text.toString().equals(binding1.PasswordAgain)){
                Toast.makeText(this, "Incorrect Password", Toast.LENGTH_LONG).show()
                Log.d("nonopassword wrong", "wrong")
            }
            else {
                val intent = Intent(this, JoinActivity2::class.java)
                intent.putExtra("email", binding1.textEmail.text.toString())
                intent.putExtra("password", binding1.textPassword.text.toString())
                startActivity(intent)
            }
        }

    }

}