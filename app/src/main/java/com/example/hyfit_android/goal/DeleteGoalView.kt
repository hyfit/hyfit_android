package com.example.hyfit_android.goal

interface DeleteGoalView {
    fun onDeleteGoalSuccess(result:String)
    fun onDeleteGoalFailure(code:Int, msg:String)
}