package com.example.hyfit_android

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface FollowRetrofitInterface {

    @GET(ApiPathConstants.FOLLOW_API_PATH + "/follower")
    fun getFollowerList(@Header("X-AUTH-TOKEN")token:String): Call<FollowResponse>

    @GET(ApiPathConstants.FOLLOW_API_PATH + "/following")
    fun getFollowingList(@Header("X-AUTH-TOKEN")token:String): Call<FollowResponse>
}