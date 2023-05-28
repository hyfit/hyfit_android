package com.example.hyfit_android.community

interface GetCommunityProfileView {
    fun onGetCommunityProfileSuccess(result: PostProfile)
    fun onGetCommunityProfileFailure(code:Int, msg:String)
}