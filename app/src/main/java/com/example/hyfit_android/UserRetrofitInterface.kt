package com.example.hyfit_android

import com.example.hyfit_android.Join.JoinReq
import com.example.hyfit_android.Login.FindPasswordReq
import com.example.hyfit_android.Login.LoginReq
//import com.example.hyfit_android.Login.LoginReq
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Query

interface UserRetrofitInterface {

    @POST("/api/user/join")
    fun join(@Body joinRequest: JoinReq): Call<UserResponse>

    @POST("/api/user/login")
    fun login(@Body loginReq: LoginReq): Call<UserResponse>


    @POST("/api/user/logout")
    fun logout(@Header("X-AUTH-TOKEN")jwt : String) :Call<UserResponse>

    @POST("/api/email/confirm")
    fun confirm(@Query("email") email:String) :Call<UserResponse>

    @PATCH("/api/user/password")
    fun editpassword(@Body passwordReq : FindPasswordReq) : Call<UserResponse>

}