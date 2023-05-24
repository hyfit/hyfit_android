package com.example.hyfit_android.exercise.exerciseWith

import com.example.hyfit_android.exercise.Exercise

interface DeleteExerciseWithView {
    fun onDeleteExerciseWithSuccess(result: Int)
    fun onDeleteExerciseWithFailure(code: Int, msg: String)
}