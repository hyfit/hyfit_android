package com.example.hyfit_android.exercise

import android.content.ContentValues.TAG
import android.util.Log
import com.example.hyfit_android.getRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ExerciseService {
    private lateinit var exerciseStartView : ExerciseStartView
    private lateinit var endExerciseView : EndExerciseView

    fun setExerciseStartView(exerciseStartView: ExerciseStartView){
        this.exerciseStartView = exerciseStartView
    }
    fun setEndExerciseView(endExerciseView: EndExerciseView){
        this.endExerciseView = endExerciseView
    }

    fun startExercise(jwt:String,exerciseStartReq: ExerciseStartReq){
        val exerciseService = getRetrofit().create(ExerciseRetrofitInterface::class.java)
        exerciseService.startExercise(jwt,exerciseStartReq).enqueue(object: Callback<ExerciseRes> {
            override fun onResponse(call: Call<ExerciseRes>, response: Response<ExerciseRes>) {
                Log.d("EXERCISESTART/SUCCESS",exerciseStartReq.toString())
                Log.d("EXERCISESTART/SUCCESS",jwt)
                Log.d("EXERCISESTART/SUCCESS", response.toString())
                val resp: ExerciseRes = response.body()!!
                Log.d("EXERCISESTART/SUCCESS", resp.toString())
                when(val code = resp.code){
                    1000-> exerciseStartView.onExerciseStartSuccess(resp.result)
                    else-> exerciseStartView.onExerciseStartFailure(code, resp.message)
                }
            }
            override fun onFailure(call: Call<ExerciseRes>, t: Throwable) {
                Log.d("EXERCISESTART/FAILURE", t.message.toString())
            }
        })
    }


    fun endExercise(exerciseEndReq: ExerciseEndReq){
        val exerciseService = getRetrofit().create(ExerciseRetrofitInterface::class.java)
        exerciseService.endExercise(exerciseEndReq).enqueue(object: Callback<ExerciseRes> {
            override fun onResponse(call: Call<ExerciseRes>, response: Response<ExerciseRes>) {
                Log.d("EXERCISEEND/SUCCESS", response.toString())
                val resp: ExerciseRes = response.body()!!
                Log.d("EXERCISEEND/SUCCESS", resp.toString())
                when(val code = resp.code){
                    1000-> endExerciseView.onEndExerciseSuccess(resp.result)
                    else-> endExerciseView.onEndExerciseFailure(code, resp.message)
                }
            }
            override fun onFailure(call: Call<ExerciseRes>, t: Throwable) {
                Log.d("EXERCISEEND/FAILURE", t.message.toString())
            }
        })
    }

}