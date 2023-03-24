package com.example.hyfit_android.goal

interface GetPlaceView {

    fun onGetPlaceSuccess(result:ArrayList<Place>)
    fun onGetPlaceFailure(code:Int, msg:String)

}