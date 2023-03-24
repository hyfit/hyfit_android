package com.example.hyfit_android.goal

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface GoalRetrofitInterface {

    @GET("/api/goal/progress")
    fun getGoalProgress(@Header("X-AUTH-TOKEN")token : String): Call<GetGoalRes>

    @GET("/api/goal/done")
    fun getGoalDone(@Header("X-AUTH-TOKEN")token : String): Call<GetGoalRes>

    @GET("/api/goal/place")
    fun getGoalPlace(@Query("type") type: String, @Query("continents") continents: String) : Call<GetPlaceRes>
}