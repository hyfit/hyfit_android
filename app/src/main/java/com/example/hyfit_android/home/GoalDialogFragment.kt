package com.example.hyfit_android.home

import android.content.DialogInterface
import android.util.Log
import androidx.fragment.app.Fragment
import com.example.hyfit_android.getRetrofit
import com.example.hyfit_android.goal.GetGoalRes
import com.example.hyfit_android.goal.GetGoalView
import com.example.hyfit_android.goal.GoalRetrofitInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GoalDialogFragment  {

    private lateinit var getGoalView: GetGoalView

    fun getGoalProgress(jwt:String){
        val postService = getRetrofit().create(GoalRetrofitInterface::class.java)
        postService.getGoalProgress(jwt).enqueue(object: Callback<GetGoalRes> {
            override fun onResponse(call: Call<GetGoalRes>, response: Response<GetGoalRes>) {
                Log.d("PROGRESSGOAL/SUCCESS", response.toString())
                val resp: GetGoalRes = response.body()!!
                Log.d("PROGRESSGOAL/SUCCESS", resp.toString())
                when(val code = resp.code){
                    1000-> getGoalView.onGetGoalSuccess(resp.result)
                    else-> getGoalView.onGetGoalFailure(code, resp.message)
                }
            }
            override fun onFailure(call: Call<GetGoalRes>, t: Throwable) {
                Log.d("PROGRESSGOAL/FAILURE", t.message.toString())
            }
        })
    }
}