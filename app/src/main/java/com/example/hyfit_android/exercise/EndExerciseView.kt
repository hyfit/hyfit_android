package com.example.hyfit_android.exercise

import com.example.hyfit_android.goal.Goal

interface EndExerciseView {
    fun onEndExerciseSuccess(result:Exercise)
    fun onEndExerciseFailure(code:Int, msg:String)
}