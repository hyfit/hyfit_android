package com.example.hyfit_android.community

interface GetFollowerView {
    fun onFollowerSuccess(result:HashMap<String, List<String>>)
    fun onFollowerFailure(code:Int, msg: String)
}