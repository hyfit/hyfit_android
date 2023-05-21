package com.example.hyfit_android


import com.example.hyfit_android.report.UserbodyReq
import retrofit2.Call
import retrofit2.http.*

interface ReportRetrofitInterface {
    @GET("/justtry")
    fun report(@Header("email") email:String): Call<ReportResponse>

    @POST("/bodydata")
    fun bodydata(@Body userbodyReq: UserbodyReq): Call<UserResponse>

    @GET("/bmi")
    fun bmi(@Header("email") email:String): Call<ReportResponse>


}