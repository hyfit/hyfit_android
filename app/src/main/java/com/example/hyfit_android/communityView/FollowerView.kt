package com.example.hyfit_android.communityView

interface FollowerView {
    fun onFollowerSuccess(code:Int, result:HashMap<String, List<String>>)
    fun onFollowerFailure(code:Int, msg: String)
}