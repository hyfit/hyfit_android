package com.example.hyfit_android.community

import com.example.hyfit_android.ApiPathConstants
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface PostRetrofitInterface {

    @POST(ApiPathConstants.POST_API_PATH + "/save")
    fun savePost(@Header("X-AUTH-TOKEN")token: String, @Body post: Post): Call<PostResponse>

    @GET(ApiPathConstants.POST_API_PATH + "/{post_id}")
    fun getOnePost(@Header("X-AUTH-TOKEN")token:String, @Path("post_id")post_id:Int): Call<PostResponse>

    @GET(ApiPathConstants.POST_API_PATH + "")
    fun getAllPosts(@Query("email") email: String): Call<GetAllPostsResponse>

    @PATCH(ApiPathConstants.POST_API_PATH + "/modify/{post_id}")
    fun modifyPost(@Header("X-AUTH-TOKEN")token: String, @Path("post_id")post_id: Int): Call<PostResponse>

}