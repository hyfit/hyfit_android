package com.example.hyfit_android.community

interface UnfollowView {
    fun onUnfollowSuccess(code:Int, result: String)
    fun onUnfollowFailure(code:Int, msg: String)
}