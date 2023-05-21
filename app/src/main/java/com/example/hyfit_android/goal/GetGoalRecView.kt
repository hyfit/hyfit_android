package com.example.hyfit_android.goal

interface GetGoalRecView {
    fun onGetGoalRecSuccess(result:ArrayList<PlaceImage>)
    fun onGetGoalRecFailure(code:Int, msg:String)
}