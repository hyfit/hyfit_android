package com.example.hyfit_android.Login

interface LogoutView {
    fun onLogoutSuccess(code:Int, msg:String)
    fun onLogoutFailure(code:Int, msg:String)
}