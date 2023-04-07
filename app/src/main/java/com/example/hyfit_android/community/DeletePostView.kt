package com.example.hyfit_android.community

interface DeletePostView {
    fun onDeletePostSuccess(result: String)
    fun onDeletePostFailure(code: Int, msg: String)
}