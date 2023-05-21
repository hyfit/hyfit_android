package com.example.hyfit_android.location

import retrofit2.Call
import retrofit2.http.*

interface LocationRetrofitInterface {
    @POST("/api/location/exercise")
    fun saveExerciseLoc(@Body locationExerciseSaveReq: LocationExerciseSaveReq) : Call<LocationExerciseRes>

    @POST("/api/location/redis-exercise")
    fun saveExerciseRedisLoc(@Body locationRedisReq: LocationRedisReq) : Call<LocationRedisRes>

    @GET("/api/location/exercise")
    fun getAllExerciseList(@Query("id") id : Int) : Call<LocationRedisRes>

    @GET("/api/location/redis-exercise")
    fun getRedisExercise(@Query("id") id : Int) : Call<LocationRedisExerciseRes>

    @GET("/api/location/redis-all")
    fun getAllRedisExercise(@Query("id") id : Int): Call<LocationRedisRes>

    @POST("/api/location/redis-alt")
    fun saveRedisAltExercise(@Body locationAltRedisReq : LocationAltRedisReq) : Call<LocationRedisRes>

}