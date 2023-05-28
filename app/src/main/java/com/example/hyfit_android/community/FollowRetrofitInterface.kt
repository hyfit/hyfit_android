package com.example.hyfit_android.community

import com.example.hyfit_android.ApiPathConstants
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface FollowRetrofitInterface {

    @POST(ApiPathConstants.FOLLOW_API_PATH + "/add")
    fun addFollow(@Header("X-AUTH-TOKEN")token: String, @Query("email")email: String): Call<FollowResponse>

    @DELETE(ApiPathConstants.FOLLOW_API_PATH + "")
    fun unfollow(@Header("X-AUTH-TOKEN")token: String, @Query("email")email: String): Call<FollowResponse>

    @GET(ApiPathConstants.FOLLOW_API_PATH + "/follower")
    fun getFollowerList(@Header("X-AUTH-TOKEN")token:String): Call<FollowListResponse>

    @GET(ApiPathConstants.FOLLOW_API_PATH + "/following")
    fun getFollowingList(@Header("X-AUTH-TOKEN")token:String): Call<FollowListResponse>
}