package com.example.hyfit_android.exercise

import com.example.hyfit_android.goal.Goal

interface DeleteExerciseView {
   fun onDeleteExerciseSuccess(result: Long)
   fun onDeleteExerciseFailure(code: Int, msg: String)

}