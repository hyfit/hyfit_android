package com.example.hyfit_android.goal

interface GetMountainView {
    fun onGetMountainSuccess(result:ArrayList<Goal>)
    fun onGetMountainFailure(code:Int, msg:String)
}