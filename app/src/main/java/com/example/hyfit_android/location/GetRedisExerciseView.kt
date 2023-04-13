package com.example.hyfit_android.location

interface GetRedisExerciseView {
    fun onGetRedisExerciseViewSuccess(result: RedisExercise)
    fun onGetRedisExerciseViewFailure(code:Int, msg:String)
}