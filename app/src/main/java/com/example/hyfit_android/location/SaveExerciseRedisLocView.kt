package com.example.hyfit_android.location

interface SaveExerciseRedisLocView {
    fun onSaveExerciseRedisLocSuccess(result: List<String>)
    fun onSaveExerciseRedisLocFailure(code:Int, msg:String)
}