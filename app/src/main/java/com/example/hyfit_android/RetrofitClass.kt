package com.example.hyfit_android


import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

fun getRetrofit(): Retrofit {
    val retrofit = Retrofit.Builder().baseUrl("https://hyfit.shop")
        .addConverterFactory(GsonConverterFactory.create()).build()

    return retrofit
}