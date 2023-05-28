package com.example.hyfit_android.UserInfo

import com.example.hyfit_android.User

interface UserGetByEmailView {
    fun onUserGetByEmailSuccess(code:Int, result: User)
    fun onUserGetByEmailFailure(code:Int, msg:String)
}