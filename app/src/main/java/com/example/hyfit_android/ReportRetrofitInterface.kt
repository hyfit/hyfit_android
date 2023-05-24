package com.example.hyfit_android

import okhttp3.ResponseBody
import retrofit2.http.GET;
import retrofit2.Call
import retrofit2.http.Header;
import retrofit2.http.Query

interface ReportRetrofitInterface {
    @GET("/rocket")
    fun report(@Header("email") email:String): Call<ReportResponse>


}
