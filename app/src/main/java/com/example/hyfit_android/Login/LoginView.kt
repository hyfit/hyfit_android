package com.example.hyfit_android.Login

interface LoginView {
    fun onLoginSuccess(code:Int, jwt:String)
    fun onLoginFailure(code:Int, msg:String)
}