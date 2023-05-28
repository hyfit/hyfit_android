package com.example.hyfit_android.community

interface DeleteCommentView {
    fun onDeleteCommentSuccess(result: String)
    fun onDeleteCommentFailure(code: Int, msg: String)
}