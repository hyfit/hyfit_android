package com.example.hyfit_android.UserInfo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.hyfit_android.databinding.ActivityEditPassword2Binding
import com.example.hyfit_android.databinding.ActivityEditPasswordBinding

class EditPasswordActivity2 : AppCompatActivity() {
    lateinit var binding:ActivityEditPassword2Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityEditPassword2Binding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}