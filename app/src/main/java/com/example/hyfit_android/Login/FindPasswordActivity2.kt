package com.example.hyfit_android.Login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.hyfit_android.databinding.ActivityFindPassword2Binding

class FindPasswordActivity2 : AppCompatActivity() {
    lateinit var binding:ActivityFindPassword2Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityFindPassword2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        var findcode:String=getCode()!!
        Log.d("findcode", findcode)

        var email: String =intent.getStringExtra("email").toString()

        binding.joinSubmit.setOnClickListener {
            var userpasscode:String=binding.typehere.text.toString()
            Log.d("userpasscode", userpasscode)
            if(userpasscode.equals(findcode)){
                val intent= Intent(this, FindPasswordActivity3::class.java)
                intent.putExtra("email", email)
                startActivity(intent)
            }
            else{
                Log.d("email not passed", "email not passed")
                Toast.makeText(this,"Code mismatch", Toast.LENGTH_LONG).show()
            }
        }

        binding.prev.setOnClickListener {
            val intent=Intent(this, FindPasswordActivity1::class.java)
            startActivity(intent)
        }


    }

    private fun getCode():String? {
        val spf = getSharedPreferences("passmail", MODE_PRIVATE)
        return spf!!.getString("passcode", "0")
    }
}