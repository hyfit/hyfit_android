package com.example.hyfit_android

import okhttp3.ResponseBody
import retrofit2.http.GET;
import retrofit2.Call
import retrofit2.http.Header;

interface ReportRetrofitInterface {
    @GET("/api/data")
    fun report(): Call<ResponseBody>


}