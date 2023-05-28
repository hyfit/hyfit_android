package com.example.hyfit_android.community

import com.example.hyfit_android.ApiPathConstants
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface PostRetrofitInterface {

    @POST(ApiPathConstants.POST_API_PATH + "/save")
    fun savePost(@Header("X-AUTH-TOKEN")token: String, @Part file : MultipartBody.Part, @Part("dto")savePostReq: SavePostReq): Call<PostResponse>

    @GET(ApiPathConstants.POST_API_PATH + "/{id}")
    fun getOnePost(@Path("id")post_id:Long, @Query("email")email: String): Call<GetOnePostRes>

    // 한 유저의 게시물 목록 조회
    @GET(ApiPathConstants.POST_API_PATH + "/all-posts")
    fun getAllPostsOfUser(@Query("email")email: String): Call<GetAllPostsOfUserRes>

    // 팔로잉 유저 게시물 조회 (운동 타입 별 검색 가능)
    @GET(ApiPathConstants.POST_API_PATH + "/following-posts")
    fun getAllPostsOfFollowingUsersWithType(@Header("X-AUTH-TOKEN")token: String, @Query("lastPostId")lastPostId: Long?=null, @Query("searchType")searchType: String?=null, @Query("size")size: Int): Call<PostPageRes>

    // 모든 유저 게시물 조회 (운동 타입 별 검색 가능)
    @GET(ApiPathConstants.POST_API_PATH + "/all-users-posts")
    fun getAllPostsOfAllUsersWithType(@Header("X-AUTH-TOKEN")token: String, @Query("lastPostId")lastPostId: Long?=null, @Query("searchType")searchType: String?=null, @Query("size")size: Int): Call<PostPageRes>

    // 게시물 내용 수정
    @PATCH(ApiPathConstants.POST_API_PATH + "/{id}/modify")
    fun modifyPost(@Header("X-AUTH-TOKEN")token: String, @Path("id")postId: Long, @Query("content")content: String): Call<PostResponse>

    @POST(ApiPathConstants.POST_API_PATH + "/{id}/like")
    fun likePost(@Header("X-AUTH-TOKEN")token: String, @Path("id")postId: Long): Call<PostLikeRes>

    @DELETE(ApiPathConstants.POST_API_PATH + "/{id}/unlike")
    fun unlikePost(@Header("X-AUTH-TOKEN")token: String, @Path("id")postId: Long): Call<DefaultCommunityRes>

    @POST(ApiPathConstants.POST_API_PATH + "/{id}/comment/save")
    fun saveComment(@Header("X-AUTH-TOKEN")token: String, @Path("id")postId: Long, @Body saveCommentReq: SaveCommentReq): Call<SaveCommentRes>

    @GET(ApiPathConstants.POST_API_PATH + "/{id}/comment")
    fun getCommentList(@Path("id")postId: Long) : Call<GetCommentListRes>
    @DELETE(ApiPathConstants.POST_API_PATH + "/{id}/comment")
    fun deleteComment(@Header("X-AUTH-TOKEN")token: String, @Path("id")postId: Long, @Query("commentId")commentId: Long): Call<DefaultCommunityRes>

    @DELETE(ApiPathConstants.POST_API_PATH + "")
    fun deletePost(@Header("X-AUTH-TOKEN")token: String, @Query("id")post_id: Long): Call<DefaultCommunityRes>
    @GET(ApiPathConstants.POST_API_PATH + "/profile")
    fun getCommunityProfile(@Query("email")email: String): Call<PostProfileRes>


}