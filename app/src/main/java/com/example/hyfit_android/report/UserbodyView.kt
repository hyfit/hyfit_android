package com.example.hyfit_android.report

interface UserbodyView {
    fun onBodySuccess(code:Int, result:String)
    fun onBodyFailure(code:Int, msg:String)
}