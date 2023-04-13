package com.example.hyfit_android.UserInfo

interface ValidView {
    fun onValidSuccess(code:Int, result:String)
    fun onValidFailure(code:Int, msg:String)
}