package com.example.hyfit_android.goal

interface GetPlacePageView {
    fun onGetPlacePageSuccess(result:Int)
    fun onGetPlacePageFailure(code:Int, msg:String)
}