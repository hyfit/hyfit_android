package com.example.hyfit_android.exercise

import com.example.hyfit_android.goal.Goal

interface GetExerciseView {
   fun onGetExerciseViewSuccess(result: Exercise)
   fun onGetExerciseViewFailure(code: Int, msg: String)

}