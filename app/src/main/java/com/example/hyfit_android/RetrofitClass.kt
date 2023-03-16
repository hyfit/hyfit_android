package com.example.hyfit_android

import androidx.databinding.ktx.BuildConfig
import retrofit2.Retrofit
import com.example.hyfit_android.BuildConfig.BASE_URL
import retrofit2.converter.gson.GsonConverterFactory


fun getRetrofit(): Retrofit {

    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    return retrofit
}