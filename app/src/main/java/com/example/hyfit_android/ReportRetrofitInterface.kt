package com.example.hyfit_android


import com.example.hyfit_android.report.UserbodyReq
import retrofit2.Call
import retrofit2.http.*

interface ReportRetrofitInterface {
    //dashboard+change그래프까지 완료
    @GET("/hyfit_report")
    fun report(@Header("email") email:String): Call<ReportResponse>

    //bodydata post 체중 키 목표체중 입력
    @POST("/bodydata")
    fun bodydata(@Body userbodyReq: UserbodyReq): Call<UserResponse>

    //@GET("/bmi")
    //fun bmi(@Header("email") email:String): Call<ReportResponse>


}
