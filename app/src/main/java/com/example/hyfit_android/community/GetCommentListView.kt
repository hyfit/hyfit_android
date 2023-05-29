package com.example.hyfit_android.community

interface GetCommentListView {
    fun onGetCommentListSuccess(result: ArrayList<PostCommentList>)
    fun onGetCommentListFailure(code:Int, msg:String)
}