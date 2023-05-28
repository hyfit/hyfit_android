package com.example.hyfit_android.exercise.exerciseWith

interface StartExerciseView {
    fun onStartExerciseViewSuccess(result: ExreciseWith)
    fun onStartExerciseViewFailure(code: Int, msg: String)
}