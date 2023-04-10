package com.example.hyfit_android

import com.example.hyfit_android.Join.JoinReq
import com.example.hyfit_android.Login.FindPasswordReq
import com.example.hyfit_android.Login.LoginReq
import com.example.hyfit_android.UserInfo.UpdatepassReq
//import com.example.hyfit_android.Login.LoginReq
import retrofit2.Call
import retrofit2.http.*

interface UserRetrofitInterface {

    @POST("/api/user/join")
    fun join(@Body joinRequest: JoinReq): Call<UserResponse>

    @POST("/api/user/login")
    fun login(@Body loginReq: LoginReq): Call<UserResponse>


    @POST("/api/user/logout")
    fun logout(@Header("X-AUTH-TOKEN")jwt : String) :Call<UserResponse>

    @POST("/api/email/confirm")
    fun confirm(@Query("email") email:String) :Call<UserResponse>

    //비밀번호 수정(분실)
    @PATCH("/api/user/forget-password")
    fun editpassword(@Body passwordReq : FindPasswordReq) : Call<UserResponse>

    //비밀번호 수정(일반)
    @PATCH("/api/user/password")
    fun passwordupdate(@Header("X-AUTH-TOKEN")jwt:String,
                       @Body updatepassReq: UpdatepassReq
    ) : Call<UserResponse>

    @GET("/api/user/password-match")
    fun passwordcheck(@Header("X-AUTH-TOKEN")jwt:String,
                       @Query("password") password:String) : Call<UserResponse>

}