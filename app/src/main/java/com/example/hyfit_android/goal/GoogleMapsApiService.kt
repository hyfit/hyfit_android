package com.example.hyfit_android.goal

import retrofit2.Call
import retrofit2.http.*

interface GoogleMapsApiService {
    @GET("geocode/json")
    fun getGeocode(
        @Query("address") address: String,
        @Query("key") apiKey: String
    ): Call<GeocodeResponse>
}