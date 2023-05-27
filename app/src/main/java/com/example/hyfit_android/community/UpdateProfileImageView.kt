package com.example.hyfit_android.community

import com.example.hyfit_android.User

interface UpdateProfileImageView {
    fun onUpdateProfileImageSuccess(result: User)
    fun onUpdateProfileImageFailure(code:Int, msg:String)
}