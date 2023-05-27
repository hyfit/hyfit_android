package com.example.hyfit_android.community

import android.util.Log
import com.example.hyfit_android.getRetrofit
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PostService {
    private lateinit var savePostView: SavePostView
    private lateinit var getOnePostView: GetOnePostView
    private lateinit var getAllPostsOfUserView: GetAllPostsOfUserView
    private lateinit var getAllPostsOfFollowingUsersView: GetAllPostsOfFollowingUsersView
    private lateinit var  getAllPostsOfAllUsersView: GetAllPostsOfAllUsersView
    private lateinit var modifyPostView: ModifyPostView
    private lateinit var likePostView: LikePostView
    private lateinit var unlikePostView: UnlikePostView
    private lateinit var saveCommentView: SaveCommentView
    private lateinit var getCommentListView: GetCommentListView
    private lateinit var deleteCommentView: DeleteCommentView
    private lateinit var deletePostView: DeletePostView
    private lateinit var getCommunityProfileView: GetCommunityProfileView


    fun setSavePostView(savePostView: SavePostView) {
        this.savePostView = savePostView
    }
    fun setGetOnePostView(getOnePostView: GetOnePostView) {
        this.getOnePostView = getOnePostView
    }
    fun setGetAllPostsOfUserView(getAllPostsOfUserView: GetAllPostsOfUserView) {
        this.getAllPostsOfUserView = getAllPostsOfUserView
    }
    fun setGetAllPostsOfFollowingUsersView(getAllPostsOfFollowingUsersView: GetAllPostsOfFollowingUsersView) {
        this.getAllPostsOfFollowingUsersView = getAllPostsOfFollowingUsersView
    }
    fun setGetAllPostsOfAllUsersView(getAllPostsOfAllUsersView: GetAllPostsOfAllUsersView) {
        this.getAllPostsOfAllUsersView = getAllPostsOfAllUsersView
    }
    fun setModifyPostView(modifyPostView: ModifyPostView) {
        this.modifyPostView = modifyPostView
    }
    fun setLikePostView(likePostView: LikePostView) {
        this.likePostView = likePostView
    }
    fun setUnlikePostView(unlikePostView: UnlikePostView) {
        this.unlikePostView = unlikePostView
    }
    fun setSaveCommentView(saveCommentView: SaveCommentView) {
        this.saveCommentView = saveCommentView
    }
    fun setGetCommentListView(getCommentListView: GetCommentListView) {
        this.getCommentListView = getCommentListView
    }
    fun setDeleteCommentView(deleteCommentView: DeleteCommentView) {
        this.deleteCommentView = deleteCommentView
    }
    fun setDeletePostView(deletePostView: DeletePostView) {
        this.deletePostView = deletePostView
    }
    fun setGetCommunityProfileView(getCommunityProfileView: GetCommunityProfileView) {
        this.getCommunityProfileView = getCommunityProfileView
    }


    fun savePost(token:String, file: MultipartBody.Part, savePostReq: SavePostReq) {
        val postService = getPostRetrofit().create(PostRetrofitInterface::class.java)
        postService.savePost(token, file,savePostReq).enqueue(object: Callback<PostResponse> {
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

    fun getOnePost(postId: Long, email:String) {
        val postService = getPostRetrofit().create(PostRetrofitInterface::class.java)
        postService.getOnePost(postId, email).enqueue(object: Callback<GetOnePostRes> {
            override fun onResponse(
                call: Call<GetOnePostRes>,
                response: Response<GetOnePostRes>) {
                Log.d("GETONEPOST/SUCCESS",response.toString())
                val resp: GetOnePostRes = response.body()!!
                Log.d("GETONEPOST/SUCCESS", resp.toString())
                when(val code = resp.code) {
                    1000 -> getOnePostView.onGetOnePostSuccess( resp.result)
                    else -> getOnePostView.onGetOnePostFailure(code, resp.message)
                }
            }

            override fun onFailure(call: Call<GetOnePostRes>, t: Throwable) {
                Log.d("GETONEPOST/FAILURE", t.message.toString())
            }
        })
    }

    fun getAllPostsOfUser(email:String) {
        val postService = getPostRetrofit().create(PostRetrofitInterface::class.java)
        postService.getAllPostsOfUser(email).enqueue(object: Callback<GetAllPostsOfUserRes> {
            override fun onResponse(
                call: Call<GetAllPostsOfUserRes>,
                response: Response<GetAllPostsOfUserRes>) {
                Log.d("GETALLPOSTS(O)/SUCCESS",response.toString())
                val resp: GetAllPostsOfUserRes = response.body()!!
                Log.d("GETALLPOSTS(O)/SUCCESS", resp.toString())
                when(val code = resp.code) {
                    1000 -> getAllPostsOfUserView.onGetAllUserPostsSuccess(resp.result)
                    else -> getAllPostsOfUserView.onGetAllUserPostsFailure(code, resp.message)
                }
            }

            override fun onFailure(call: Call<GetAllPostsOfUserRes>, t: Throwable) {
                Log.d("GETALLPOSTS/FAILURE", t.message.toString())
            }
        })
    }

    fun getAllPostsOfFollowingUsersWithType(token: String, lastPostId: Long?, searchType: String?, size: Int) {
        val postService = getPostRetrofit().create(PostRetrofitInterface::class.java)
        postService.getAllPostsOfFollowingUsersWithType(token, lastPostId, searchType,size).enqueue(object: Callback<PostPageRes> {
            override fun onResponse(
                call: Call<PostPageRes>,
                response: Response<PostPageRes>) {
                Log.d("GETALLPOSTS(F)/SUCCESS",response.toString())
                val resp: PostPageRes = response.body()!!
                Log.d("GETALLPOSTS(F)/SUCCESS", resp.toString())
                when(val code = resp.code) {
                    1000 -> getAllPostsOfFollowingUsersView.onGetAllPostsOfFollowingUsersWithTypeSuccess(resp.result)
                    else -> getAllPostsOfFollowingUsersView.onGetAllPostsOfFollowingUsersWithTypeFailure(code, resp.message)
                }
            }

            override fun onFailure(call: Call<PostPageRes>, t: Throwable) {
                Log.d("GETALLPOSTS/FAILURE", t.message.toString())
            }
        })
    }

    fun getAllPostsOfAllUsersWithType(token: String,  lastPostId: Long?, searchType: String?, size: Int) {
        val postService = getPostRetrofit().create(PostRetrofitInterface::class.java)
        postService.getAllPostsOfAllUsersWithType(token, lastPostId, searchType,size).enqueue(object: Callback<PostPageRes> {
            override fun onResponse(
                call: Call<PostPageRes>,
                response: Response<PostPageRes>) {
                Log.d("GETALLPOSTS(A)/SUCCESS",response.toString())
                val resp: PostPageRes = response.body()!!
                Log.d("GETALLPOSTS(A)/SUCCESS", resp.toString())
                when(val code = resp.code) {
                    1000 -> getAllPostsOfAllUsersView.onGetAllPostsOfAllUsersWithTypeSuccess(resp.result)
                    else -> getAllPostsOfAllUsersView.onGetAllPostsOfAllUsersWithTypeFailure(code, resp.message)
                }
            }

            override fun onFailure(call: Call<PostPageRes>, t: Throwable) {
                Log.d("GETALLPOSTS(A)/FAILURE", t.message.toString())
            }
        })
    }

    fun modifyPost(token:String, postId: Long, content: String) {
        val postService = getPostRetrofit().create(PostRetrofitInterface::class.java)
        postService.modifyPost(token, postId, content).enqueue(object: Callback<PostResponse> {
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

    fun likePost(token:String, postId: Long) {
        val postService = getPostRetrofit().create(PostRetrofitInterface::class.java)
        postService.likePost(token, postId).enqueue(object: Callback<PostLikeRes> {
            override fun onResponse(
                call: Call<PostLikeRes>,
                response: Response<PostLikeRes>) {
                Log.d("LIKEPOST/SUCCESS",response.toString())
                val resp: PostLikeRes = response.body()!!
                Log.d("LIKEPOST/SUCCESS", resp.toString())
                when(val code = resp.code) {
                    1000 -> likePostView.onLikePostSuccess(resp.result)
                    else -> likePostView.onLikePostFailure(code, resp.message)
                }
            }

            override fun onFailure(call: Call<PostLikeRes>, t: Throwable) {
                Log.d("LIKEPOST/FAILURE", t.message.toString())
            }
        })
    }

    fun unlikePost(token:String, postId: Long) {
        val postService = getPostRetrofit().create(PostRetrofitInterface::class.java)
        postService.unlikePost(token, postId).enqueue(object: Callback<DefaultCommunityRes> {
            override fun onResponse(
                call: Call<DefaultCommunityRes>,
                response: Response<DefaultCommunityRes>) {
                Log.d("UNLIKEPOST/SUCCESS",response.toString())
                val resp: DefaultCommunityRes = response.body()!!
                Log.d("UNLIKEPOST/SUCCESS", resp.toString())
                when(val code = resp.code) {
                    1000 -> unlikePostView.onUnlikePostSuccess(resp.result)
                    else -> unlikePostView.onUnlikePostFailure(code, resp.message)
                }
            }

            override fun onFailure(call: Call<DefaultCommunityRes>, t: Throwable) {
                Log.d("UNLIKEPOST/FAILURE", t.message.toString())
            }
        })
    }

    fun saveComment(token:String, postId: Long, saveCommentReq: SaveCommentReq) {
        val postService = getPostRetrofit().create(PostRetrofitInterface::class.java)
        postService.saveComment(token, postId,saveCommentReq).enqueue(object: Callback<SaveCommentRes> {
            override fun onResponse(
                call: Call<SaveCommentRes>,
                response: Response<SaveCommentRes>) {
                Log.d("SAVECOMMENT/SUCCESS",response.toString())
                val resp: SaveCommentRes = response.body()!!
                Log.d("SAVECOMMENT/SUCCESS", resp.toString())
                when(val code = resp.code) {
                    1000 -> saveCommentView.onSaveCommentSuccess(resp.result)
                    else -> saveCommentView.onSaveCommentFailure(code, resp.message)
                }
            }

            override fun onFailure(call: Call<SaveCommentRes>, t: Throwable) {
                Log.d("SAVECOMMENT/FAILURE", t.message.toString())
            }
        })
    }

    fun getCommentList(postId: Long) {
        val postService = getPostRetrofit().create(PostRetrofitInterface::class.java)
        postService.getCommentList(postId).enqueue(object: Callback<GetCommentListRes> {
            override fun onResponse(
                call: Call<GetCommentListRes>,
                response: Response<GetCommentListRes>) {
                Log.d("GETCOMMENTLIST/SUCCESS",response.toString())
                val resp: GetCommentListRes = response.body()!!
                Log.d("GETCOMMENTLIST/SUCCESS", resp.toString())
                when(val code = resp.code) {
                    1000 -> getCommentListView.onGetCommentListSuccess(resp.result)
                    else -> getCommentListView.onGetCommentListFailure(code, resp.message)
                }
            }

            override fun onFailure(call: Call<GetCommentListRes>, t: Throwable) {
                Log.d("GETCOMMENTLIST/FAILURE", t.message.toString())
            }
        })
    }

    fun deleteComment(token:String, postId: Long, commentId: Long) {
        val postService = getPostRetrofit().create(PostRetrofitInterface::class.java)
        postService.deleteComment(token, postId, commentId).enqueue(object: Callback<DefaultCommunityRes> {
            override fun onResponse(
                call: Call<DefaultCommunityRes>,
                response: Response<DefaultCommunityRes>) {
                Log.d("DELETECOMMENT/SUCCESS",response.toString())
                val resp: DefaultCommunityRes = response.body()!!
                Log.d("DELETECOMMENT/SUCCESS", resp.toString())
                when(val code = resp.code) {
                    1000 -> deleteCommentView.onDeleteCommentSuccess(resp.result)
                    else -> deleteCommentView.onDeleteCommentFailure(code, resp.message)
                }
            }

            override fun onFailure(call: Call<DefaultCommunityRes>, t: Throwable) {
                Log.d("DELETECOMMENT/FAILURE", t.message.toString())
            }
        })
    }



    fun deletePost(token:String, postId: Long) {
        val postService = getPostRetrofit().create(PostRetrofitInterface::class.java)
        postService.deletePost(token, postId).enqueue(object: Callback<DefaultCommunityRes> {
            override fun onResponse(
                call: Call<DefaultCommunityRes>,
                response: Response<DefaultCommunityRes>) {
                Log.d("DELETEPOST/SUCCESS",response.toString())
                val resp: DefaultCommunityRes = response.body()!!
                Log.d("DELETEPOST/SUCCESS", resp.toString())
                when(val code = resp.code) {
                    1000 -> deletePostView.onDeletePostSuccess(resp.result)
                    else -> deletePostView.onDeletePostFailure(code, resp.message)
                }
            }

            override fun onFailure(call: Call<DefaultCommunityRes>, t: Throwable) {
                Log.d("DELETEPOST/FAILURE", t.message.toString())
            }
        })
    }

    fun getCommunityProfile(email:String) {
        val postService = getPostRetrofit().create(PostRetrofitInterface::class.java)
        postService.getCommunityProfile(email).enqueue(object: Callback<PostProfileRes> {
            override fun onResponse(
                call: Call<PostProfileRes>,
                response: Response<PostProfileRes>) {
                Log.d("GETCMPROFILE/SUCCESS",response.toString())
                val resp: PostProfileRes = response.body()!!
                Log.d("GETCMPROFILE/SUCCESS", resp.toString())
                when(val code = resp.code) {
                    1000 -> getCommunityProfileView.onGetCommunityProfileSuccess(resp.result)
                    else -> getCommunityProfileView.onGetCommunityProfileFailure(code, resp.message)
                }
            }

            override fun onFailure(call: Call<PostProfileRes>, t: Throwable) {
                Log.d("GETCMPROFILE/FAILURE", t.message.toString())
            }
        })
    }


}