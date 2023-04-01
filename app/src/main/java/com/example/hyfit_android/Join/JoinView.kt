package com.example.hyfit_android.Join

interface JoinView {
    fun onJoinSuccess(code:Int, result:String)
    fun onJoinFailure(code:Int, message:String)
}