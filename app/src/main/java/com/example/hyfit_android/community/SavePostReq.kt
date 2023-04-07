package com.example.hyfit_android.community

data class SavePostReq (
        val postId: Int,
        val email: String,
        val exercise_data_id: Int,
        val content: String
        )