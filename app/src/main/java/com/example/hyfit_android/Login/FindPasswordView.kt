package com.example.hyfit_android.Login

interface FindPasswordView {
    fun onFindSuccess(code:Int, result:String)
    fun onFindFailure(code:Int, msg:String)
}