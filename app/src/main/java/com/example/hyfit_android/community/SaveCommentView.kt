package com.example.hyfit_android.community

interface SaveCommentView {
    fun onSaveCommentSuccess(result: PostComment)
    fun onSaveCommentFailure(code:Int, msg:String)
}