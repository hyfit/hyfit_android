package com.example.hyfit_android.exercise.exerciseWith

interface ReadyExerciseWithView {
    fun onReadyExerciseWithSuccess(result: Boolean)
    fun onReadyExerciseWithFailure(code: Int, msg: String)
}