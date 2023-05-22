package com.example.hyfit_android

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

fun getreportRetrofit(): Retrofit {

    val rpretrofit = Retrofit.Builder()
        .baseUrl("http://10.0.2.2:5000")
        .addConverterFactory(GsonConverterFactory.create())
        .client(OkHttpClient())
        .build()

    return rpretrofit
}