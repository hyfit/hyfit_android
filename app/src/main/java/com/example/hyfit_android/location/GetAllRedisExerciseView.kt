package com.example.hyfit_android.location

interface GetAllRedisExerciseView {
    fun onGetAllRedisExerciseSuccess(result: ArrayList<String>)
    fun onGetAllRedisExerciseFailure(code:Int, msg:String)
}