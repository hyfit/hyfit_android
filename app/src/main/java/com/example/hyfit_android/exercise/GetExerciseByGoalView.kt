package com.example.hyfit_android.exercise

import com.example.hyfit_android.goal.Goal

interface GetExerciseByGoalView {
   fun onGetExerciseByGoalSuccess(result: List<Exercise>)
   fun onGetExerciseByGoalFailure(code: Int, msg: String)

}