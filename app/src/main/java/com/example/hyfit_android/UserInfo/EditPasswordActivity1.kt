package com.example.hyfit_android.UserInfo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import com.example.hyfit_android.Login.LoginReq
import com.example.hyfit_android.MainActivity
import com.example.hyfit_android.UserRetrofitService
import com.example.hyfit_android.databinding.ActivityEditPasswordBinding

class EditPasswordActivity1 : AppCompatActivity(), PasswordCheckView, PasswordUpdateView {
    lateinit var binding:ActivityEditPasswordBinding
    lateinit var progressBar: ProgressBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityEditPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        progressBar=binding.progressBar


        binding.prev.setOnClickListener {
            Log.d("good", "Good")
        }

        binding.joinNext.setOnClickListener{
            passwordcheck()
//            val intent=Intent(this, MainActivity::class.java)
//            startActivity(intent)
        }

        binding.saveNext.setOnClickListener {
            passwordupdate()
        }
    }

    private fun passwordcheck() {
        if (binding.textpassword.text.toString().isEmpty()) {
            Toast.makeText(this, "Fill in the blank", Toast.LENGTH_LONG).show()
            Log.d("test", "fill in all the blanks")
            return
        }

        val jwt: String? = getJwt()
        Log.d("jwtjwt", jwt.toString())
        val password:String=binding.textpassword.text.toString()
        val usService = UserRetrofitService()
        usService.setpasswordCheckView(this)
        usService.passwordcheck(jwt, password)
    }

    private fun getJwt():String?{
        val spf = getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)
        return spf!!.getString("jwt","0")
    }

    override fun onCheckSuccess(code: Int, msg: String) {
        when(code){
            1000->{
                progressBar.visibility = ProgressBar.GONE
                binding.joinNext.visibility=View.GONE
                binding.joinTextNext.visibility=View.GONE
                binding.newpassword.visibility=View.VISIBLE
                binding.passwordagain.visibility=View.VISIBLE
                binding.textnewpassword.visibility=View.VISIBLE
                binding.textPasswordAgain.visibility=View.VISIBLE
                binding.saveNext.visibility=View.VISIBLE
                binding.saveTextNext.visibility=View.VISIBLE
                Log.d("CheckSuccess", code.toString())
            }
            else->{
                Log.d("error", code.toString())
                if(code.toString()=="2003"){
                    Toast.makeText(this, "Incorrect password", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onCheckFailure(code: Int, msg: String) {
        Log.d("CheckFailure", "sadasad")
    }

    private fun passwordupdate() {
        if (binding.textnewpassword.text.toString().isEmpty() || binding.textPasswordAgain.text.toString().isEmpty()) {
            Toast.makeText(this, "Fill in all the blanks", Toast.LENGTH_LONG).show()
            Log.d("test", "fill in all the blanks")
            return
        }
        else if(binding.textnewpassword.text.toString()!=binding.textPasswordAgain.text.toString()){
            Toast.makeText(this, "Password mismatch", Toast.LENGTH_LONG).show()
            Log.d("pwpw", "pw two different")
            return
        }

        val jwt: String? = getJwt()
        Log.d("jwtjwt", jwt.toString())
        val password:String=binding.textnewpassword.text.toString()
        val usService = UserRetrofitService()
        usService.setpasswordUpdateView(this)
        usService.passwordupdate(jwt, UpdatepassReq(password))
    }

    override fun onUpdateSuccess(code: Int, msg: String) {
        when(code){
            1000->{
                Log.d("updateSuccess", code.toString())
                val intent=Intent(this, MainActivity::class.java)
                intent.putExtra("showSetFragment", true)
                startActivity(intent)
            }
            else->{
                Log.d("error", code.toString())
                if(code.toString()=="2007"){
                    Toast.makeText(this, "password format error", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onUpdateFailure(code: Int, msg: String) {
        Log.d("updateFail", "failfail")
    }
}