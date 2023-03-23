package com.example.hyfit_android.Join

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.hyfit_android.databinding.ActivityJoinEmailBinding

class JoinEmailActivity : AppCompatActivity() {

    lateinit var binding:ActivityJoinEmailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.joinSubmit.setOnClickListener {
            val intent= Intent(this, JoinActivity4::class.java)
            intent.putExtra("1", binding.code1.text.toString())
            intent.putExtra("2", binding.code2.text.toString())
            intent.putExtra("3", binding.code3.text.toString())
            intent.putExtra("4", binding.code4.text.toString())
            startActivity(intent)
        }
    }
}