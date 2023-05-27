package com.example.hyfit_android.community


interface GetAllPostsOfFollowingUsersView {
    fun onGetAllPostsOfFollowingUsersWithTypeSuccess(result: Slice)
    fun onGetAllPostsOfFollowingUsersWithTypeFailure(code:Int, msg:String)
}