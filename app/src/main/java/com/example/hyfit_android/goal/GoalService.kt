package com.example.hyfit_android.goal

import android.util.Log
import com.example.hyfit_android.getRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GoalService {
    private lateinit var getGoalView: GetGoalView
    private lateinit var getDoneGoalView: GetDoneGoalView
    private lateinit var saveGoalView: SaveGoalView
    private lateinit var deleteGoalView: DeleteGoalView

    fun setGetGoalView(getGoalView: GetGoalView){
        this.getGoalView = getGoalView
    }

    fun setGetDoneGoalView(getDoneGoalView: GetDoneGoalView){
        this.getDoneGoalView = getDoneGoalView
    }
    fun setSaveGoalView(saveGoalView: SaveGoalView){
        this.saveGoalView = saveGoalView
    }
    fun setDeleteGoalView(deleteGoalView: DeleteGoalView){
        this.deleteGoalView = deleteGoalView
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

    fun saveGoal(jwt : String,saveGoalReq: SaveGoalReq){
        val postService = getRetrofit().create(GoalRetrofitInterface::class.java)
        postService.saveGoal(jwt,saveGoalReq).enqueue(object : Callback<SaveGoalRes>{
            override fun onResponse(call : Call<SaveGoalRes>, response : Response<SaveGoalRes>){
                Log.d("SAVEGOAL/SUCCESS", response.toString())
                val resp: SaveGoalRes = response.body()!!
                Log.d("SAVEGOAL/SUCCESS", resp.toString())
                when(val code = resp.code){
                    1000-> saveGoalView.onSaveGoalSuccess(resp.result)
                    else-> saveGoalView.onSaveGoalFailure(code, resp.message)
                }
            }
            override fun onFailure(call: Call<SaveGoalRes>, t: Throwable) {
                Log.d("SAVEGOAL/FAILURE", t.message.toString())
            }
        })
    }
    fun deleteGoal(jwt : String,id : Long){
        val goalService = getRetrofit().create(GoalRetrofitInterface::class.java)
        goalService.deleteGoal(jwt,id).enqueue(object : Callback<DeleteGoalRes>{
            override fun onResponse(call : Call<DeleteGoalRes>, response : Response<DeleteGoalRes>){
                Log.d("DELETEGOAL/SUCCESS", response.toString())
                val resp: DeleteGoalRes = response.body()!!
                Log.d("DELETEGOAL/SUCCESS", resp.toString())
                when(val code = resp.code){
                    1000-> deleteGoalView.onDeleteGoalSuccess(resp.result)
                    else-> deleteGoalView.onDeleteGoalFailure(code, resp.message)
                }
            }
            override fun onFailure(call: Call<DeleteGoalRes>, t: Throwable) {
                Log.d("DELETEGOAL/FAILURE", t.message.toString())
            }
        })
    }

    }

