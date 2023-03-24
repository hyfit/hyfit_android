package com.example.hyfit_android.Join

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

import com.example.hyfit_android.RetrofitService
import com.example.hyfit_android.databinding.ActivityJoin3Binding


class JoinActivity3 : AppCompatActivity(), JoinView {

    lateinit var binding3 : ActivityJoin3Binding
    lateinit var gender:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding3= ActivityJoin3Binding.inflate(layoutInflater)
        setContentView(binding3.root)

        binding3.prev.setOnClickListener {
            val intent=Intent(this, JoinActivity2::class.java)
            startActivity(intent)
        }

        val rdgg=binding3.rdgroup
        val rman=binding3.man
        val rwoman=binding3.woman
        val next=binding3.joinNext

        rdgg.setOnCheckedChangeListener { radioGroup,  i->
            if(i==binding3.rdgroup.id){
                when(i) {
                    binding3.man.id -> Log.d("남자", binding3.man.text.toString())
                    binding3.woman.id -> Log.d("여자", binding3.woman.text.toString())
                }
            }
        }


//        Log.d("hereherehere", phone)
//        Log.d("hereherehereemail", email)
          next.setOnClickListener{

            //val intent = Intent(this, JoinActivity4::class.java )
            if(rman.isChecked){
                gender=rman.text.toString()
                Log.d("hereherehereman", rman.text.toString())
            }
            else{
                gender=rwoman.text.toString()
                Log.d("herehereherewoman", rwoman.text.toString())
            }
            join()
           // startActivity(intent)
        }
    }
    private fun join(){
        val nickName:String=binding3.textName.text.toString()
        var password:String = intent.getStringExtra("password").toString()
        var email:String = intent.getStringExtra("email").toString()
        var name:String=intent.getStringExtra("name").toString()
        var phone:String=intent.getStringExtra("phone").toString()
        var birth:String=intent.getStringExtra("birth").toString()
        val usService = RetrofitService()
        usService.setJoinView(this)
        usService.join(JoinReq(name, email, password, nickName, phone, birth, gender))
    }

    override fun onJoinSuccess() {
        val intent=Intent(this, JoinActivity4::class.java)
        startActivity(intent)
    }

    override fun onJoinFailure() {
        Log.d("JoinFailure", "sadsad")
    }
}