package com.example.hyfit_android

import com.example.hyfit_android.Join.JoinReq
import com.example.hyfit_android.Login.FindPasswordReq
import com.example.hyfit_android.Login.LoginReq
import com.example.hyfit_android.UserInfo.UpdateUserReq
import com.example.hyfit_android.UserInfo.UpdatepassReq
import okhttp3.MultipartBody
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

    //유저 정보 get
    @GET("/api/user")
    fun userget(@Header("X-AUTH-TOKEN")jwt:String) :Call<GetResponse>

    //유저 정보 수정
    @PATCH("/api/user")
    fun userupdate(@Header("X-AUTH-TOKEN")jwt:String,
                   @Body updateUserReq: UpdateUserReq) :Call<GetResponse>

    @GET("/api/user/valid")
    fun valid(@Header("X-AUTH-TOKEN")jwt:String) :Call<UserResponse>

    @POST("/api/user/profile-image")
    fun updateProfileImage(@Header("X-AUTH-TOKEN")jwt:String, @Part file : MultipartBody.Part): Call<GetResponse>

}