package com.example.hyfit_android.exercise

import com.example.hyfit_android.goal.Goal

interface GetAllExerciseView {
   fun onGetAllExerciseSuccess(result: List<Exercise>)
   fun onGetAllExerciseFailure(code: Int, msg: String)

}