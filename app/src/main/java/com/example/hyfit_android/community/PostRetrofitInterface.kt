package com.example.hyfit_android.community

import com.example.hyfit_android.ApiPathConstants
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface PostRetrofitInterface {
    @GET(ApiPathConstants.POST_API_PATH + "/post")
    fun getOnePost(@Header("X-AUTH-TOKEN")token:String, @Query("post_id")post_id:Int): Call<PostResponse>

}