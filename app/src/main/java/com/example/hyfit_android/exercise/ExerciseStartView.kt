package com.example.hyfit_android.exercise

interface ExerciseStartView {
    fun onExerciseStartSuccess(result:Exercise)
    fun onExerciseStartFailure(code:Int, msg:String)
}