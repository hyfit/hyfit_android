package com.example.hyfit_android.communityView

interface FollowingView {
    fun onFollowingSuccess(code:Int, result: HashMap<String, List<String>>)
    fun onFollowingFailure(code:Int, msg:String)
}