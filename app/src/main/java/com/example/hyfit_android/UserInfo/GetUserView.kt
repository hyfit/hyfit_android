package com.example.hyfit_android.UserInfo

import com.example.hyfit_android.User

interface GetUserView {
    fun onUserSuccess(code:Int, result:User)
    fun onUserFailure(code:Int, msg:String)
}