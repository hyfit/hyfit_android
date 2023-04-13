package com.example.hyfit_android.location

interface SaveExerciseLocView {
    fun onSaveExerciseLocSuccess(result: Location)
    fun onSaveExerciseLocFailure(code:Int, msg:String)
}