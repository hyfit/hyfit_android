package com.example.hyfit_android.community


interface GetAllPostsOfAllUsersView {
    fun onGetAllPostsOfAllUsersWithTypeSuccess(result: Slice)
    fun onGetAllPostsOfAllUsersWithTypeFailure(code:Int, msg:String)
}