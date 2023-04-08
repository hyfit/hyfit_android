package com.example.hyfit_android.Join

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ProgressBar
import android.widget.Toast

import com.example.hyfit_android.UserRetrofitService
import com.example.hyfit_android.databinding.ActivityJoin3Binding


class JoinActivity3 : AppCompatActivity(), JoinEmailView {

    lateinit var binding3 : ActivityJoin3Binding
    lateinit var gender:String
    lateinit var name:String
    lateinit var birth:String
    lateinit var phone:String
    lateinit var email:String
    lateinit var password:String

    lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding3= ActivityJoin3Binding.inflate(layoutInflater)
        setContentView(binding3.root)
        password= intent.getStringExtra("password").toString()
        email= intent.getStringExtra("email").toString()
        name=intent.getStringExtra("name").toString()
        phone=intent.getStringExtra("phone").toString()
        birth=intent.getStringExtra("birth").toString()

        binding3.prev.setOnClickListener {
            val intent=Intent(this, JoinActivity2::class.java)
            startActivity(intent)
        }

        val rdgg=binding3.rdgroup
        val rman=binding3.man
        val rwoman=binding3.woman
        val next=binding3.joinNext

        progressBar = binding3.progressBar

        rdgg.setOnCheckedChangeListener { radioGroup,  i->
            if(i==binding3.rdgroup.id){
                when(i) {
                    binding3.man.id -> Log.d("남자", binding3.man.text.toString())
                    binding3.woman.id -> Log.d("여자", binding3.woman.text.toString())
                }
            }
        }

          next.setOnClickListener{
//            if(rman.isChecked){
//                gender=rman.text.toString()
//                Log.d("hereherehereman", rman.text.toString())
//            }
//            else{
//                gender=rwoman.text.toString()
//                Log.d("herehereherewoman", rwoman.text.toString())
//            }

            confirm(email)
              progressBar.visibility = ProgressBar.VISIBLE
        }
    }

    private fun confirm(email:String?){
        if (binding3.textName.text.toString().isEmpty() || gender==null){
            Toast.makeText(this,"Fill in all the blanks", Toast.LENGTH_LONG).show()
            Log.d("test", "fill in all the blanks")
            return
        }
        //var email:String = intent.getStringExtra("email").toString()
        val usService = UserRetrofitService()
        usService.setJoinEmailView(this)
        usService.confirm(email)
    }

    override fun onEmailSuccess(code:Int, result:String) {
        val intent=Intent(this, JoinEmailActivity::class.java)
        intent.putExtra("name", name)
        intent.putExtra("birth", birth)
        intent.putExtra("phone", phone)
        intent.putExtra("email", email)
        intent.putExtra("password", password)
        intent.putExtra("gender", gender)
        intent.putExtra("nickName", binding3.textName.text.toString())
        val spf=getSharedPreferences("mail", MODE_PRIVATE)
        val editor=spf.edit()
        editor.putString("code", result)
        editor.apply()
        editor.commit()
        progressBar.visibility = ProgressBar.GONE
        startActivity(intent)
    }

    override fun onEmailFailure(code:Int, msg:String) {
        progressBar.visibility = ProgressBar.GONE
        Log.d("EmailFailure", "sadsad")
    }


//    private fun join(){
//        val nickName:String=binding3.textName.text.toString()
//        var password:String = intent.getStringExtra("password").toString()
//        var email:String = intent.getStringExtra("email").toString()
//        var name:String=intent.getStringExtra("name").toString()
//        var phone:String=intent.getStringExtra("phone").toString()
//        var birth:String=intent.getStringExtra("birth").toString()
//        val usService = UserRetrofitService()
//        usService.setJoinView(this)
//        usService.join(JoinReq(name, email, password, nickName, phone, birth, gender))
//    }
//
//    override fun onJoinSuccess() {
//        val intent=Intent(this, JoinEmailActivity::class.java)
//        startActivity(intent)
//    }
//
//    override fun onJoinFailure() {
//        Log.d("JoinFailure", "sadsad")
//    }

}