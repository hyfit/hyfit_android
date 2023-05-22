package com.example.hyfit_android.goal

interface GetBuildingView {
    fun onGetBuildingSuccess(result:ArrayList<Goal>)
    fun onGetBuildingFailure(code:Int, msg:String)
}