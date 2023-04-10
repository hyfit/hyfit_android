package com.example.hyfit_android.UserInfo

interface PasswordUpdateView {
    fun onUpdateSuccess(code:Int, msg: String)
    fun onUpdateFailure(code:Int, msg:String)
}