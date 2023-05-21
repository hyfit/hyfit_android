package com.example.hyfit_android.Join

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.example.hyfit_android.Login.LoginActivity
import com.example.hyfit_android.Login.LoginReq
import com.example.hyfit_android.ReportRetrofitService
import com.example.hyfit_android.UserRetrofitService
import com.example.hyfit_android.databinding.ActivityBodydataBinding
import com.example.hyfit_android.databinding.ActivityJoin3Binding
import com.example.hyfit_android.report.UserbodyReq
import com.example.hyfit_android.report.UserbodyView


class JoinActivity4 : AppCompatActivity(), UserbodyView {
    lateinit var binding4: ActivityBodydataBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding4 = ActivityBodydataBinding.inflate(layoutInflater)
        setContentView(binding4.root)
        val email= intent.getStringExtra("email").toString()


        binding4.complete.setOnClickListener{
            val weight:String=binding4.textWeight.text.toString()
            val height:String=binding4.textHeight.text.toString()
            val goalweight:String=binding4.textGoalweight.text.toString()
            val rpService = ReportRetrofitService()
            rpService.setBodyView(this)
            rpService.bodydata(UserbodyReq(email, weight, height, goalweight))
            binding4.progressBar.visibility = ProgressBar.VISIBLE
        }


    }
    override fun onBodySuccess(code: Int, result: String) {
        Log.d("BodyDatagoodGood", "goodgoodgood")
        val intent= Intent(this, JoinActivity5::class.java)
        binding4.progressBar.visibility = ProgressBar.GONE
        startActivity(intent)
    }

    override fun onBodyFailure(code: Int, result:String) {
        Log.d("BodyDataFailure", result.toString())
    }
}