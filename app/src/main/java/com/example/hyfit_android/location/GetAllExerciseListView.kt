package com.example.hyfit_android.location

import com.example.hyfit_android.exercise.Exercise

interface GetAllExerciseListView {
    fun onGetAllExerciseListSuccess(result: List<String>)
    fun onGetAllExerciseListFailure(code:Int, msg:String)
}