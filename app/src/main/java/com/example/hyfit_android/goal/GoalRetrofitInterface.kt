package com.example.hyfit_android.goal

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface GoalRetrofitInterface {

    @GET("/api/goal/progress")
    fun getGoalProgress(@Header("X-AUTH-TOKEN")token : String): Call<GetGoalRes>

    @GET("/api/goal/done")
    fun getGoalDone(@Header("X-AUTH-TOKEN")token : String): Call<GetGoalRes>

    @GET("/api/goal/place")
    fun getGoalPlace(@Query("type") type: String, @Query("continents") continents: String) : Call<GetPlaceRes>

    @POST("/api/goal/add")
    fun saveGoal(@Header("X-AUTH-TOKEN")token : String, @Body saveGoalReq: SaveGoalReq) : Call<SaveGoalRes>

    @DELETE("/api/goal")
    fun deleteGoal(@Header("X-AUTH-TOKEN")token : String, @Query("id") id: Long) :Call<DeleteGoalRes>

    @GET("/api/goal/place/page-size")
    fun getPageSize(@Query("type") type: String, @Query("continents") continents: String) : Call<Int>

    @GET("/api/goal/place-page")
    fun getGoalPlacePage(@Query("type") type: String, @Query("continents") continents: String, @Query("page") page:Int) : Call<GetPlaceRes>
}