package com.example.hyfit_android.goal

interface GetDoneGoalView {
    fun onGetDoneGoalSuccess(result:ArrayList<Goal>)
    fun onGetDoneGoalFailure(code:Int, msg:String)
}