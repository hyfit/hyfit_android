package com.example.hyfit_android.UserInfo

interface PasswordCheckView {
    fun onCheckSuccess(code:Int, msg: String)
    fun onCheckFailure(code:Int, msg:String)
}