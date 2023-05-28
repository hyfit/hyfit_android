package com.example.hyfit_android.community

import android.util.Log
import com.example.hyfit_android.getRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PostService {
    private lateinit var savePostView: SavePostView
    private lateinit var getOnePostView: GetOnePostView
    private lateinit var getAllUserPostsView: GetAllUserPostsView
    private lateinit var modifyPostView: ModifyPostView
    private lateinit var deletePostView: DeletePostView
    private lateinit var likePostView: LikePostView
    private lateinit var unlikePostView: UnlikePostView

    fun setSavePostView(savePostView: SavePostView) {
        this.savePostView = savePostView
    }
    fun setGetOnePostView(getOnePostView: GetOnePostView) {
        this.getOnePostView = getOnePostView
    }
    fun setLikePostView(likePostView: LikePostView) {
        this.likePostView = likePostView
    }
    fun setunlikePostView(unlikePostView: UnlikePostView) {
        this.unlikePostView = unlikePostView
    }
    fun setGetAllPostsView(getAllUserPostsView: GetAllUserPostsView) {
        this.getAllUserPostsView = getAllUserPostsView
    }
    fun setModifyPostView(modifyPostView: ModifyPostView) {
        this.modifyPostView = modifyPostView
    }
    fun setDeletePostView(deletePostView: DeletePostView) {
        this.deletePostView = deletePostView
    }

    fun savePost(token:String, savePostReq: SavePostReq) {
        val postService = getRetrofit().create(PostRetrofitInterface::class.java)
        postService.savePost(token, savePostReq).enqueue(object: Callback<PostResponse> {
            override fun onResponse(
                call: Call<PostResponse>,
                response: Response<PostResponse>) {
                Log.d("SAVEPOST/SUCCESS",response.toString())
                val resp: PostResponse = response.body()!!
                Log.d("SAVEPOST/SUCCESS", resp.toString())
                when(val code = resp.code) {
                    1000 -> savePostView.onSavePostSuccess(resp.result)
                    else -> savePostView.onSavePostFailure(code, resp.message)
                }
            }

            override fun onFailure(call: Call<PostResponse>, t: Throwable) {
                Log.d("SAVEPOST/FAILURE", t.message.toString())
            }
        })
    }

    fun getOnePost(token:String, post_id: Long, email:String) {
        val postService = getRetrofit().create(PostRetrofitInterface::class.java)
        postService.getOnePost(token, post_id, email).enqueue(object: Callback<GetOnePostResponse> {
            override fun onResponse(
                call: Call<GetOnePostResponse>,
                response: Response<GetOnePostResponse>) {
                Log.d("GETONEPOST/SUCCESS",response.toString())
                val resp: GetOnePostResponse = response.body()!!
                Log.d("GETONEPOST/SUCCESS", resp.toString())
                when(val code = resp.code) {
                    1000 -> getOnePostView.onGetOnePostSuccess( resp.result)
                    else -> getOnePostView.onGetOnePostFailure(code, resp.message)
                }
            }

            override fun onFailure(call: Call<GetOnePostResponse>, t: Throwable) {
                Log.d("GETONEPOST/FAILURE", t.message.toString())
            }
        })
    }

    fun like(token:String, id: Long) {
        val postService = getRetrofit().create(PostRetrofitInterface::class.java)
        postService.like(token, id).enqueue(object: Callback<LikePostResponse> {
            override fun onResponse(
                call: Call<LikePostResponse>,
                response: Response<LikePostResponse>) {
                Log.d("LIKEPOST/SUCCESS",response.toString())
                val resp: LikePostResponse = response.body()!!
                Log.d("LIKEPOST/SUCCESS", resp.toString())
                when(val code = resp.code) {
                    1000 -> likePostView.onLikeSuccess(resp.result)
                    2303 -> likePostView.onLikeFailure(code)
                }
            }

            override fun onFailure(call: Call<LikePostResponse>, t: Throwable) {
                Log.d("GETONEPOST/FAILURE", t.message.toString())
            }
        })
    }

    fun unlike(token:String, id: Long) {
        val postService = getRetrofit().create(PostRetrofitInterface::class.java)
        postService.unlike(token, id).enqueue(object: Callback<UnlikePostResponse> {
            override fun onResponse(
                call: Call<UnlikePostResponse>,
                response: Response<UnlikePostResponse>) {
                Log.d("UNLIKEPOST/SUCCESS",response.toString())
                val resp: UnlikePostResponse = response.body()!!
                Log.d("UnLIKEPOST/SUCCESS", resp.toString())
                when(val code = resp.code) {
                    1000 -> unlikePostView.onUnlikeSuccess(resp.result)
                    else -> unlikePostView.onUnlikeFailure(code)
                }
            }

            override fun onFailure(call: Call<UnlikePostResponse>, t: Throwable) {
                Log.d("GETONEPOST/FAILURE", t.message.toString())
            }
        })
    }

    fun getAllUserPosts(email:String) {
        val postService = getRetrofit().create(PostRetrofitInterface::class.java)
        postService.getAllUserPosts(email).enqueue(object: Callback<GetAllUserPostsResponse> {
            override fun onResponse(
                call: Call<GetAllUserPostsResponse>,
                response: Response<GetAllUserPostsResponse>) {
                Log.d("GETALLPOSTS/SUCCESS",response.toString())
                val resp: GetAllUserPostsResponse = response.body()!!
                Log.d("GETALLPOSTS/SUCCESS", resp.toString())
                when(val code = resp.code) {
                    1000 -> getAllUserPostsView.onGetAllUserPostsSuccess(resp.result)
                    else -> getAllUserPostsView.onGetAllUserPostsFailure(code, resp.message)
                }
            }

            override fun onFailure(call: Call<GetAllUserPostsResponse>, t: Throwable) {
                Log.d("GETALLPOSTS/FAILURE", t.message.toString())
            }
        })
    }

    fun modifyPost(token:String, postId: Long, modifyPostReq: ModifyPostReq) {
        val postService = getRetrofit().create(PostRetrofitInterface::class.java)
        postService.modifyPost(token, postId, modifyPostReq).enqueue(object: Callback<PostResponse> {
            override fun onResponse(
                call: Call<PostResponse>,
                response: Response<PostResponse>) {
                Log.d("MODIFYPOST/SUCCESS",response.toString())
                val resp: PostResponse = response.body()!!
                Log.d("MODIFYPOST/SUCCESS", resp.toString())
                when(val code = resp.code) {
                    1000 -> modifyPostView.onModifyPostSuccess(resp.result)
                    else -> modifyPostView.onModifyPostFailure(code, resp.message)
                }
            }

            override fun onFailure(call: Call<PostResponse>, t: Throwable) {
                Log.d("MODIFYPOST/FAILURE", t.message.toString())
            }
        })
    }

    fun deletePost(token:String, postId: Long) {
        val postService = getRetrofit().create(PostRetrofitInterface::class.java)
        postService.deletePost(token, postId).enqueue(object: Callback<DeletePostResponse> {
            override fun onResponse(
                call: Call<DeletePostResponse>,
                response: Response<DeletePostResponse>) {
                Log.d("DELETEPOST/SUCCESS",response.toString())
                val resp: DeletePostResponse = response.body()!!
                Log.d("DELETEPOST/SUCCESS", resp.toString())
                when(val code = resp.code) {
                    1000 -> deletePostView.onDeletePostSuccess(resp.result)
                    else -> deletePostView.onDeletePostFailure(code, resp.message)
                }
            }

            override fun onFailure(call: Call<DeletePostResponse>, t: Throwable) {
                Log.d("DELETEPOST/FAILURE", t.message.toString())
            }
        })
    }

}