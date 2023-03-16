package com.example.hyfit_android.goal

interface GetGoalView {
    fun onGetGoalSuccess(result:ArrayList<Goal>)
    fun onGetGoalFailure(code:Int, msg:String)
}