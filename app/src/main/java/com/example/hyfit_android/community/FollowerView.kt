package com.example.hyfit_android.community

interface FollowerView {
    fun onFollowerSuccess(code:Int, result:HashMap<String, List<String>>)
    fun onFollowerFailure(code:Int, msg: String)
}