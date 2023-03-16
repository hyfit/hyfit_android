package com.example.hyfit_android.goal

import android.util.Log
import com.example.hyfit_android.getRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GoalService {
    private lateinit var getGoalView: GetGoalView
    private lateinit var getDoneGoalView: GetDoneGoalView

    fun setGetGoalView(getGoalView: GetGoalView){
        this.getGoalView = getGoalView
    }

    fun setGetDoneGoalView(getDoneGoalView: GetDoneGoalView){
        this.getDoneGoalView = getDoneGoalView
    }


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

    fun getGoalDone(jwt:String){
        val postService = getRetrofit().create(GoalRetrofitInterface::class.java)
        postService.getGoalDone(jwt).enqueue(object: Callback<GetGoalRes> {
            override fun onResponse(call: Call<GetGoalRes>, response: Response<GetGoalRes>) {
                Log.d("DONEGOAL/SUCCESS", response.toString())
                val resp: GetGoalRes = response.body()!!
                Log.d("DONEGOAL/SUCCESS", resp.toString())
                when(val code = resp.code){
                    1000-> getDoneGoalView.onGetDoneGoalSuccess(resp.result)
                    else-> getDoneGoalView.onGetDoneGoalFailure(code, resp.message)
                }
            }
            override fun onFailure(call: Call<GetGoalRes>, t: Throwable) {
                Log.d("DONEGOAL/FAILURE", t.message.toString())
            }
        })
    }

}