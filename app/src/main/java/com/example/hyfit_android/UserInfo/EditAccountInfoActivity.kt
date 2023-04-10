package com.example.hyfit_android.UserInfo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.FragmentManager
import com.example.hyfit_android.MainActivity
import com.example.hyfit_android.R
import com.example.hyfit_android.SetFragment
import com.example.hyfit_android.databinding.ActivityEditAccountInfoBinding
import com.example.hyfit_android.databinding.ActivityMainBinding
import com.example.hyfit_android.databinding.FragmentSetBinding

class EditAccountInfoActivity : AppCompatActivity(){
    lateinit var binding:ActivityEditAccountInfoBinding
    lateinit var bindingset:FragmentSetBinding
    lateinit var bindingmain:ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityEditAccountInfoBinding.inflate(layoutInflater)
        bindingset=FragmentSetBinding.inflate(layoutInflater)
        bindingmain= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.showemail.text="daun0405@naver.com" //getuser해서 거기 이메일로 대체
        binding.showname.text="정유진" //getuser해서 거기 이름으로 대체

        binding.prev.setOnClickListener {
            Log.d("prev", "prevprev")
            val intent=Intent(this, MainActivity::class.java)
            intent.putExtra("showSetFragment", true)
            startActivity(intent)
//            val fragment = SetFragment()
//            supportFragmentManager.beginTransaction()
//                .replace(R.id.fragment_container, fragment)
//                .addToBackStack("SetFragment")
//                .commit()

//            fragmentTransaction.replace(R.id.fragment_container, fragment)
//            fragmentTransaction.addToBackStack(null)
//            fragmentTransaction.commit()
        }


    }
}