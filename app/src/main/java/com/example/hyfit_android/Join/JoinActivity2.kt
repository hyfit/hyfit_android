package com.example.hyfit_android.Join

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.hyfit_android.Login.LoginActivity
import com.example.hyfit_android.databinding.ActivityJoin2Binding

class JoinActivity2 : AppCompatActivity() {


    lateinit var binding2 : ActivityJoin2Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding2= ActivityJoin2Binding.inflate(layoutInflater)
        setContentView(binding2.root)
        binding2.prev.setOnClickListener {
            val intent=Intent(this, JoinActivity1::class.java)
            startActivity(intent)
        }

        var password:String = intent.getStringExtra("password").toString()
        var email:String = intent.getStringExtra("email").toString()
        Log.d("hereherehere", email)
        binding2.joinNext.setOnClickListener{
            if (binding2.textBirth.text.toString().isEmpty() || binding2.textName.text.toString().isEmpty() || binding2.textPhoneNumber.text.toString().isEmpty()){
                Toast.makeText(this,"Fill in all the blanks", Toast.LENGTH_LONG).show()
                Log.d("test", "fill in all the blanks")
            }
            else{
                val intent = Intent(this, JoinActivity3::class.java)
                intent.putExtra("name", binding2.textName.text.toString())
                intent.putExtra("birth", binding2.textBirth.text.toString())
                intent.putExtra("phone", binding2.textPhoneNumber.text.toString())
                intent.putExtra("email", email)
                intent.putExtra("password", password)
                startActivity(intent)
            }
        }
    }
}