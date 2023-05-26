package com.example.hyfit_android.community

import com.example.hyfit_android.ApiPathConstants
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface PostRetrofitInterface {

    @POST(ApiPathConstants.POST_API_PATH + "/save")
    fun savePost(@Header("X-AUTH-TOKEN")token: String, @Body savePostReq: SavePostReq): Call<PostResponse>

    @GET(ApiPathConstants.POST_API_PATH + "/{post_id}")
    fun getOnePost(
        @Header("X-AUTH-TOKEN") token: String,
        @Path("post_id") postId: Long,
        @Query("email") email: String
    ): Call<GetOnePostResponse>

    @GET(ApiPathConstants.POST_API_PATH + "")
    fun getAllUserPosts(@Query("email")email: String): Call<GetAllUserPostsResponse>

    //팔로잉 유저 게시물 조회

    @PATCH(ApiPathConstants.POST_API_PATH + "/{post_id}")
    fun modifyPost(@Header("X-AUTH-TOKEN")token: String, @Path("post_id")post_id: Long, @Body modifyPostReq: ModifyPostReq): Call<PostResponse>

    @DELETE(ApiPathConstants.POST_API_PATH + "")
    fun deletePost(@Header("X-AUTH-TOKEN")token: String, @Path("post_id")post_id: Long): Call<DeletePostResponse>

    @POST("/api/post/{id}/like")
    fun like(
        @Header("X-AUTH-TOKEN") token: String,
        @Path("id") id: Long
    ): Call<LikePostResponse>

    @DELETE("/api/post/{id}/unlike")
    fun unlike(
        @Header("X-AUTH-TOKEN") token: String,
        @Path("id") id: Long
    ):Call<UnlikePostResponse>

    @POST("/api/follow/add?email={email}")
    fun follow(
        @Header("X-AUTH-TOKEN") token: String,
        @Query("email") id: Long
    ): Call<FollowResponse>
}