package com.example.hyfit_android.goal

import retrofit2.Call
import retrofit2.http.*

interface GoalRetrofitInterface {

    @GET("/api/goal/progress")
    fun getGoalProgress(@Header("X-AUTH-TOKEN")token : String): Call<GetGoalRes>

    @GET("/api/goal/done")
    fun getGoalDone(@Header("X-AUTH-TOKEN")token : String): Call<GetGoalRes>

    @GET("/api/goal/place")
    fun getGoalPlace(@Query("type") type: String, @Query("continents") continents: String) : Call<GetPlaceRes>

    @POST("/api/goal/add")
    fun saveGoal(@Header("X-AUTH-TOKEN")token : String, @Body saveGoalReq: SaveGoalReq) : Call<SaveGoalRes>

    @PATCH("/api/goal")
    fun modifyGoal(@Header("X-AUTH-TOKEN")token : String, @Query("id") id:Long,@Query("gain") gain:String) : Call<SaveGoalRes>

    @GET("/api/goal/mountain")
    fun getMountain(@Header("X-AUTH-TOKEN")token : String)  : Call<GetGoalRes>

    @GET("/api/goal/building")
    fun getBuilding(@Header("X-AUTH-TOKEN")token : String)  : Call<GetGoalRes>

    @DELETE("/api/goal")
    fun deleteGoal(@Header("X-AUTH-TOKEN")token : String, @Query("id") id: Long) :Call<DeleteGoalRes>

    @GET("/api/goal/place/page-size")
    fun getPageSize(@Query("type") type: String, @Query("continents") continents: String) : Call<GetPlacePageRes>

    @GET("/api/goal/place-page")
    fun getGoalPlacePage(@Query("type") type: String, @Query("continents") continents: String, @Query("page") page:Int) : Call<GetPlaceRes>

    @GET("/api/goal/place-rec")
    fun getGoalRec(@Header("X-AUTH-TOKEN")token : String) : Call<GetGoalImageRes>

   // @GET("/api/image/place/{placeId}")
  //  fun getPlaceImage(@Header("X-AUTH-TOKEN")token : String,  @Path("placeId") placeId: Long) : Call<GetGoalImageRes>
}