package com.example.hyfit_android.Join

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.hyfit_android.UserRetrofitService
import com.example.hyfit_android.databinding.ActivityJoinEmailBinding

class JoinEmailActivity : AppCompatActivity(), JoinView {

    lateinit var binding:ActivityJoinEmailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityJoinEmailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //var usercode:String=binding.typehere.text.toString()
        var servercode:String=getCode()!!

        Log.d("servercode", servercode)





        binding.joinSubmit.setOnClickListener {
            var usercode:String=binding.typehere.text.toString()
            Log.d("usercode", usercode)
            if(usercode.equals(servercode)){
                join()
            }
            else{
                Toast.makeText(this,"Code mismatch", Toast.LENGTH_LONG).show()
            }

        }
    }



    private fun join(){
        val nickName:String=intent.getStringExtra("nickName").toString()
        var password:String = intent.getStringExtra("password").toString()
        var email:String = intent.getStringExtra("email").toString()
        var name:String=intent.getStringExtra("name").toString()
        var phone:String=intent.getStringExtra("phone").toString()
        var birth:String=intent.getStringExtra("birth").toString()
        var gender:String=intent.getStringExtra("gender").toString()
        val usService = UserRetrofitService()
        usService.setJoinView(this)
        usService.join(JoinReq(name, email, password, nickName, phone, birth, gender))
    }
    private fun getCode():String? {
        val spf = getSharedPreferences("mail", MODE_PRIVATE)
        return spf!!.getString("code", "0")
    }
    override fun onJoinSuccess(code:Int, result:String) {
        Log.d("JoinGoodGood", "goodgoodgood")
        val intent=Intent(this, JoinActivity4::class.java)
        startActivity(intent)
    }

    override fun onJoinFailure(code:Int, message:String) {
        Log.d("JoinFailure", message)
    }
}