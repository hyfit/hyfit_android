package com.example.hyfit_android.community

interface GetFollowerView {
    fun onFollowerSuccess(result:List<String>)
    fun onFollowerFailure(code:Int, msg: String)
}