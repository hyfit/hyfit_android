package com.example.hyfit_android.Join

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ProgressBar
import android.widget.Toast
import com.example.hyfit_android.UserRetrofitService
import com.example.hyfit_android.databinding.ActivityJoinEmailBinding

class JoinEmailActivity : AppCompatActivity(), JoinView {

    lateinit var binding:ActivityJoinEmailBinding
    lateinit var progressBar: ProgressBar
    lateinit var email:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityJoinEmailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //var usercode:String=binding.typehere.text.toString()
        var servercode:String=getCode()!!

        Log.d("servercode", servercode)

        progressBar = binding.progressBar



        binding.joinSubmit.setOnClickListener {
            var usercode:String=binding.typehere.text.toString()
            Log.d("usercode", usercode)
            if(usercode.equals(servercode)){
                join()
                progressBar.visibility = ProgressBar.VISIBLE
            }
            else{
                Toast.makeText(this,"Code mismatch", Toast.LENGTH_LONG).show()
            }

        }
    }



    private fun join(){
        val nickName:String=intent.getStringExtra("nickName").toString()
        var password:String = intent.getStringExtra("password").toString()
        email= intent.getStringExtra("email").toString()
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
        progressBar.visibility = ProgressBar.GONE
        val intent=Intent(this, JoinActivity4::class.java)
        intent.putExtra("email", email)
        startActivity(intent)
    }

    override fun onJoinFailure(code:Int, message:String, result :String) {
        progressBar.visibility = ProgressBar.GONE
        Log.d("JoinFailure", message)
        Log.d("JoinFailure : Result", result)
    }
}