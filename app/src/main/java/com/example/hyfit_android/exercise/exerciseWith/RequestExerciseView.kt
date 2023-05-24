package com.example.hyfit_android.exercise.exerciseWith

interface RequestExerciseView {
    fun onRequestExerciseSuccess(result: ExreciseWith)
    fun onRequestExerciseFailure(code: Int, msg: String)
}