package com.example.hyfit_android.exercise.exerciseWith

import retrofit2.Call
import retrofit2.http.*

interface ExerciseWithInterface {

    @GET("/api/exercise-with")
    fun getExerciseWith(@Query("exerciseWithId") exerciseWithId : Int) : Call<ExerciseWithRes>

    @POST("/api/exercise-with/request")
    fun requestExercise(@Header("X-AUTH-TOKEN")token : String,@Body exerciseWithReq1 : ExerciseWithReq1) : Call<ExerciseWithRes>

    @POST("/api/exercise-with/start")
    fun startExercise(@Header("X-AUTH-TOKEN")token : String,@Body exerciseWithReq : ExerciseWithReq) : Call<ExerciseWithRes>

    @DELETE("/api/exercise-with")
    fun deleteExerciseWith(@Query("exerciseWithId") exerciseWithId : Int) : Call<DeleteExerciseRes>

    @GET("/api/exercise-with/is-ready")
    fun isReadyExerciseWith(@Query("exerciseWithId") exerciseWithId : Int) : Call<ReadyExerciseRes>



}