package com.example.hyfit_android.community

interface GetCommentListView {
    fun onGetCommentListSuccess(result: PostCommentList)
    fun onGetCommentListFailure(code:Int, msg:String)
}