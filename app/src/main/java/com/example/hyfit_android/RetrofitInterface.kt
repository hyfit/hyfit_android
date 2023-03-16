package com.example.hyfit_android

import com.example.hyfit_android.Join.JoinReq
//import com.example.hyfit_android.Login.LoginReq
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface RetrofitInterface {

    @POST("/api/user/join")
    fun join(@Body joinRequest: JoinReq): Call<UserResponse>

//    @POST("/api/user/login")
//    fun login(@Body loginReq: LoginReq): Call<UserResponse>
}