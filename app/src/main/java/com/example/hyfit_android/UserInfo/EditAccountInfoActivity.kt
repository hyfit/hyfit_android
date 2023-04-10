package com.example.hyfit_android.UserInfo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import com.example.hyfit_android.*
import com.example.hyfit_android.Login.LoginReq
import com.example.hyfit_android.databinding.ActivityEditAccountInfoBinding
import com.example.hyfit_android.databinding.ActivityMainBinding
import com.example.hyfit_android.databinding.FragmentSetBinding

class EditAccountInfoActivity : AppCompatActivity(), GetUserView{
    lateinit var binding:ActivityEditAccountInfoBinding
    lateinit var bindingset:FragmentSetBinding
    lateinit var bindingmain:ActivityMainBinding
    lateinit var progressBar: ProgressBar

    lateinit var nickName:String
    lateinit var birthdate:String
    lateinit var phone:String
    lateinit var profile_img:String
    lateinit var introduce:String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityEditAccountInfoBinding.inflate(layoutInflater)
        bindingset=FragmentSetBinding.inflate(layoutInflater)
        bindingmain= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        progressBar = binding.progressBar
        profile_img=""

        init()
        progressBar.visibility = ProgressBar.GONE
        var nickName=binding.editnickname.text



        binding.prev.setOnClickListener {
            Log.d("prev", "prevprev")
            val intent=Intent(this, MainActivity::class.java)
            intent.putExtra("showSetFragment", true)
            startActivity(intent)

        }

        binding.saveNext.setOnClickListener {
            userupdate()
        }
        binding.saveTextNext.setOnClickListener {
            userupdate()
        }



    }

    private fun init(){
        var birth:String?=intent.getStringExtra("birth")
        progressBar.visibility = ProgressBar.VISIBLE
        binding.showname.text=intent.getStringExtra("name")
        binding.showemail.text=intent.getStringExtra("email")
        binding.editmessage.setText(intent.getStringExtra("introduce"))
        binding.editnickname.setText(intent.getStringExtra("nickName"))
        if (birth != null) {
            if(birth.length==5){
                birth="0"+birth
            }
        }
        binding.editbirthdate.setText(birth)
        binding.editphonenumber.setText(intent.getStringExtra("phone"))
        nickName=intent.getStringExtra("nickName").toString()
        birthdate=birth.toString()
        phone=intent.getStringExtra("phone").toString()
        introduce=intent.getStringExtra("introduce").toString()
    }

    private fun userupdate(){
        nickName=binding.editnickname.text.toString()
        phone=binding.editphonenumber.text.toString()
        birthdate=binding.editbirthdate.text.toString()
        introduce=binding.editmessage.text.toString()

        val jwt:String?=getJwt()
        val usService = UserRetrofitService()
        usService.setgetuserView(this)
        usService.userupdate(jwt,UpdateUserReq(nickName,phone,birthdate,profile_img,introduce))
    }

    private fun getJwt():String?{
        val spf = getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)
        return spf!!.getString("jwt","0")
    }

    override fun onUserSuccess(code: Int, result: User) {
        when(code){
            1000->{
                Log.d("userupdateSuccess", code.toString())
                val intent=Intent(this, MainActivity::class.java)
                intent.putExtra("showSetFragment", true)
                startActivity(intent)
            }
        }

    }

    override fun onUserFailure(code: Int, msg: String) {
        Log.d("userupdateFail", "userupdatefail sadsad")

    }
}