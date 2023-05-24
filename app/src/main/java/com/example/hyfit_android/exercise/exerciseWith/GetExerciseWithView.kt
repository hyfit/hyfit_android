package com.example.hyfit_android.exercise.exerciseWith

interface GetExerciseWithView {
    fun onGetExerciseWithSuccess(result: ExreciseWith)
    fun onGetExerciseWithFailure(code: Int, msg: String)
}