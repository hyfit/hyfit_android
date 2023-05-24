package com.example.hyfit_android.report

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.example.hyfit_android.Join.JoinActivity5
import com.example.hyfit_android.MainActivity
import com.example.hyfit_android.ReportRetrofitService
import com.example.hyfit_android.databinding.ActivityBodydataBinding

class UserbodyActivity: AppCompatActivity(), UserbodyView {
    lateinit var binding: ActivityBodydataBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBodydataBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val email:String=getEmail()!!

        binding.complete.setOnClickListener{
            val weight:String=binding.textWeight.text.toString()
            val height:String=binding.textHeight.text.toString()
            val goalweight:String=binding.textGoalweight.text.toString()
            val rpService = ReportRetrofitService()
            rpService.setBodyView(this)
            rpService.bodydata(UserbodyReq(email, weight, height,goalweight))
            binding.progressBar.visibility = ProgressBar.VISIBLE
        }
    }

    private fun getEmail():String?{
        val spf = getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)
        return spf!!.getString("email","0")
    }




    override fun onBodySuccess(code: Int, result: String) {
        Log.d("BodyDatagoodGood", "goodgoodgood")
        //val intent= Intent(this, MainActivity::class.java)
        binding.progressBar.visibility = ProgressBar.GONE
        //startActivity(intent)
        finish()
    }

    override fun onBodyFailure(code: Int, result:String) {
        Log.d("BodyDataFailure", result.toString())
        finish()
    }
}