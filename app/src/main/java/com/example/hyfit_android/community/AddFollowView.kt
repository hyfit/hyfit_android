package com.example.hyfit_android.community

interface AddFollowView {

    fun onAddFollowSuccess(result: String)
    fun onAddFollowFailure(code:Int, msg: String)
}