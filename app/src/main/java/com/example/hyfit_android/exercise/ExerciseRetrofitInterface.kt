package com.example.hyfit_android.exercise

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface ExerciseRetrofitInterface {
    @POST("/api/exercise/start")
    fun startExercise(@Header("X-AUTH-TOKEN")token : String, @Body exerciseStartReq: ExerciseStartReq) : Call<ExerciseRes>

    @POST("/api/exercise/end")
    fun endExercise(@Body exerciseEndReq: ExerciseEndReq) : Call<ExerciseRes>

    @GET("/api/exercise/goal")
    fun getExerciseByGoal(@Query("goalId") goalId : Long) : Call<ExerciseListRes>
    @GET("/api/exercise")
    fun getExercise(@Query("exerciseId") exerciseId : Long) : Call<ExerciseRes>


}