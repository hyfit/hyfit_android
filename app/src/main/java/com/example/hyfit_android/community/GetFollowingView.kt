package com.example.hyfit_android.community

interface GetFollowingView {
    fun onFollowingSuccess(code:Int, result: HashMap<String, List<String>>)
    fun onFollowingFailure(code:Int, msg:String)
}