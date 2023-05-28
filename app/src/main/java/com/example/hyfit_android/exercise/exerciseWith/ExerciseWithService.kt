package com.example.hyfit_android.exercise.exerciseWith

import android.util.Log
import com.example.hyfit_android.exercise.ExerciseRes
import com.example.hyfit_android.getRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ExerciseWithService {
    private lateinit var deleteExerciseWithView: DeleteExerciseWithView
    private lateinit var getExerciseWithView: GetExerciseWithView
    private lateinit var readyExerciseWithView: ReadyExerciseWithView
    private lateinit var requestExerciseView: RequestExerciseView
    private lateinit var startExerciseView: StartExerciseView

    fun setDeleteExerciseWithView(deleteExerciseWithView: DeleteExerciseWithView){
        this.deleteExerciseWithView = deleteExerciseWithView
    }
    fun setGetExerciseWithView(getExerciseWithView: GetExerciseWithView){
        this.getExerciseWithView = getExerciseWithView
    }
    fun setReadyExerciseWithView(readyExerciseWithView: ReadyExerciseWithView){
        this.readyExerciseWithView =readyExerciseWithView
    }
    fun setRequestExerciseView(requestExerciseView: RequestExerciseView){
        this.requestExerciseView = requestExerciseView
    }
    fun setStartExerciseView(startExerciseView: StartExerciseView){
        this.startExerciseView = startExerciseView
    }

    fun getExerciseWith(exerciseWithId : Int){
        val exerciseWithService = getRetrofit().create(ExerciseWithInterface::class.java)
        exerciseWithService.getExerciseWith(exerciseWithId).enqueue(object: Callback<ExerciseWithRes> {
            override fun onResponse(call: Call<ExerciseWithRes>, response: Response<ExerciseWithRes>) {
                Log.d("EXERCISESTART/SUCCESS",exerciseWithId.toString())
                Log.d("EXERCISESTART/SUCCESS", response.toString())
                val resp: ExerciseWithRes = response.body()!!
                Log.d("EXERCISESTART/SUCCESS", resp.toString())
                when(val code = resp.code){
                    1000-> getExerciseWithView.onGetExerciseWithSuccess(resp.result)
                    else-> getExerciseWithView.onGetExerciseWithFailure(code, resp.message)
                }
            }
            override fun onFailure(call: Call<ExerciseWithRes>, t: Throwable) {
                Log.d("EXERCISESTART/FAILURE", t.message.toString())
            }
        })
    }
    fun requestExercise(token : String, exerciseWithReq1 : ExerciseWithReq1){
        val exerciseWithService = getRetrofit().create(ExerciseWithInterface::class.java)
        exerciseWithService.requestExercise(token, exerciseWithReq1).enqueue(object: Callback<ExerciseWithRes> {
            override fun onResponse(call: Call<ExerciseWithRes>, response: Response<ExerciseWithRes>) {
                Log.d("EXERCISESTART/SUCCESS", response.toString())
                val resp: ExerciseWithRes = response.body()!!
                Log.d("EXERCISESTART/SUCCESS", resp.toString())
                when(val code = resp.code){
                    1000-> requestExerciseView.onRequestExerciseSuccess(resp.result)
                    else-> requestExerciseView.onRequestExerciseFailure(code, resp.message)
                }
            }
            override fun onFailure(call: Call<ExerciseWithRes>, t: Throwable) {
                Log.d("EXERCISESTART/FAILURE", t.message.toString())
            }
        })
    }
    fun startExercise(token : String,exerciseWithReq : ExerciseWithReq){
        val exerciseWithService = getRetrofit().create(ExerciseWithInterface::class.java)
        exerciseWithService.startExercise(token,exerciseWithReq).enqueue(object: Callback<ExerciseWithRes> {
            override fun onResponse(call: Call<ExerciseWithRes>, response: Response<ExerciseWithRes>) {
                Log.d("EXERCISESTART/SUCCESS", response.toString())
                val resp: ExerciseWithRes = response.body()!!
                Log.d("EXERCISESTART/SUCCESS", resp.toString())
                when(val code = resp.code){
                    1000-> startExerciseView.onStartExerciseViewSuccess(resp.result)
                    else-> startExerciseView.onStartExerciseViewFailure(code, resp.message)
                }
            }
            override fun onFailure(call: Call<ExerciseWithRes>, t: Throwable) {
                Log.d("EXERCISESTART/FAILURE", t.message.toString())
            }
        })
    }
    fun deleteExerciseWith(exerciseWithId : Int){
        val exerciseWithService = getRetrofit().create(ExerciseWithInterface::class.java)
        exerciseWithService.deleteExerciseWith(exerciseWithId).enqueue(object: Callback<DeleteExerciseRes> {
            override fun onResponse(call: Call<DeleteExerciseRes>, response: Response<DeleteExerciseRes>) {
                Log.d("EXERCISESTART/SUCCESS", response.toString())
                val resp: DeleteExerciseRes = response.body()!!
                Log.d("EXERCISESTART/SUCCESS", resp.toString())
                when(val code = resp.code){
                    1000-> deleteExerciseWithView.onDeleteExerciseWithSuccess(resp.result)
                    else-> deleteExerciseWithView.onDeleteExerciseWithFailure(code, resp.message)
                }
            }
            override fun onFailure(call: Call<DeleteExerciseRes>, t: Throwable) {
                Log.d("EXERCISESTART/FAILURE", t.message.toString())
            }
        })
    }
    fun isReadyExerciseWith(exerciseWithId : Int){
        val exerciseWithService = getRetrofit().create(ExerciseWithInterface::class.java)
        exerciseWithService.isReadyExerciseWith(exerciseWithId).enqueue(object: Callback<ReadyExerciseRes> {
            override fun onResponse(call: Call<ReadyExerciseRes>, response: Response<ReadyExerciseRes>) {
                Log.d("EXERCISESTART/SUCCESS", response.toString())
                val resp: ReadyExerciseRes = response.body()!!
                Log.d("EXERCISESTART/SUCCESS", resp.toString())
                when(val code = resp.code){
                    1000-> readyExerciseWithView.onReadyExerciseWithSuccess(resp.result)
                    else-> readyExerciseWithView.onReadyExerciseWithFailure(code, resp.message)
                }
            }
            override fun onFailure(call: Call<ReadyExerciseRes>, t: Throwable) {
                Log.d("EXERCISESTART/FAILURE", t.message.toString())
            }
        })
    }
}