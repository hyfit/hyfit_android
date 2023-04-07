package com.example.hyfit_android.Join

interface JoinEmailView {
    fun onEmailSuccess(code:Int, result:String)
    fun onEmailFailure(code:Int, msg:String)
}