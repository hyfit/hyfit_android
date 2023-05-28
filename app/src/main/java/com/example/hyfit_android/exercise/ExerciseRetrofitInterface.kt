package com.example.hyfit_android.exercise

import retrofit2.Call
import retrofit2.http.*

interface ExerciseRetrofitInterface {
    @POST("/api/exercise/start")
    fun startExercise(@Header("X-AUTH-TOKEN")token : String, @Body exerciseStartReq: ExerciseStartReq) : Call<ExerciseRes>

    @POST("/api/exercise/end")
    fun endExercise(@Body exerciseEndReq: ExerciseEndReq) : Call<ExerciseRes>

    @GET("/api/exercise/goal")
    fun getExerciseByGoal(@Query("goalId") goalId : Long) : Call<ExerciseListRes>
    @GET("/api/exercise")
    fun getExercise(@Query("exerciseId") exerciseId : Long) : Call<ExerciseRes>

    @DELETE("/api/exercise")
    fun deleteExercise(@Query("exerciseId") exerciseId : Long) :  Call<ExerciseDeleteRes>

    @GET("/api/exercise/all")
    fun getAllExercise(@Header("X-AUTH-TOKEN")token : String) : Call<ExerciseListRes>
}