package com.example.hyfit_android.goal

interface SaveGoalView {
    fun onSaveGoalSuccess(result:Goal)
    fun onSaveGoalFailure(code:Int, msg:String)
}