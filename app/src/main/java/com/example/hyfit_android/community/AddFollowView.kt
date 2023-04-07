package com.example.hyfit_android.community

interface AddFollowView {

    fun onAddFollowSuccess(code:Int, result: String)
    fun onAddFollowFailure(code:Int, msg: String)
}