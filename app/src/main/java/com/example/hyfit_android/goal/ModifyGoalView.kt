package com.example.hyfit_android.goal

interface ModifyGoalView {
    fun onModifyGoalSuccess(result:Goal)
    fun onModifyGoalFailure(code:Int, msg:String)
}