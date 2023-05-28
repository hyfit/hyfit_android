package com.example.hyfit_android.community

import android.util.Log
import com.example.hyfit_android.getRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowService {
    private lateinit var getFollowerView: GetFollowerView
    private lateinit var getFollowingView: GetFollowingView
    private lateinit var addFollowView: AddFollowView
    private lateinit var unfollowView: UnfollowView

    fun setFollowerView(getFollowerView: GetFollowerView){
        this.getFollowerView = getFollowerView
    }
    fun setFollowingView(getFollowingView: GetFollowingView){
        this.getFollowingView = getFollowingView
    }
    fun setAddFollowView(addFollowView: AddFollowView){
        this.addFollowView = addFollowView;
    }
    fun setUnfollowView(unfollowView: UnfollowView){
        this.unfollowView = unfollowView;
    }

    fun addFollow(token:String, email:String) {
        val followService = getRetrofit().create(FollowRetrofitInterface::class.java)
        followService.addFollow(token, email).enqueue(object: Callback<FollowResponse> {
            override fun onResponse(
                call: Call<FollowResponse>,
                response: Response<FollowResponse>
            ) {
                Log.d("ADDFOLLOW/SUCCESS",response.toString())
                val resp: FollowResponse = response.body()!!
                Log.d("ADDFOLLOW/SUCCESS", resp.toString())
                when(val code = resp.code) {
                    1000 -> addFollowView.onAddFollowSuccess(resp.result)
                    else -> addFollowView.onAddFollowFailure(code, resp.message)
                }
            }

            override fun onFailure(call: Call<FollowResponse>, t: Throwable) {
                Log.d("ADDFOLLOW/FAILURE", t.message.toString())
            }

        })
    }

    fun unfollow(token:String, email:String) {
        val followService = getRetrofit().create(FollowRetrofitInterface::class.java)
        followService.unfollow(token, email).enqueue(object: Callback<DefaultCommunityRes> {
            override fun onResponse(
                call: Call<DefaultCommunityRes>,
                response: Response<DefaultCommunityRes>
            ) {
                Log.d("UNFOLLOW/SUCCESS", response.toString())
                val resp: DefaultCommunityRes = response.body()!!
                Log.d("UNFOLLOW/SUCCESS", resp.toString())
                when (val code = resp.code) {
                    1000 -> unfollowView.onUnfollowSuccess(resp.result)
                    else -> unfollowView.onUnfollowFailure(code, resp.message)
                }
            }

            override fun onFailure(call: Call<DefaultCommunityRes>, t: Throwable) {
                Log.d("UNFOLLOW/FAILURE", t.message.toString())
            }

        })
    }

    fun getFollowerList(token:String) {
        val followService = getRetrofit().create(FollowRetrofitInterface::class.java)
        followService.getFollowerList(token).enqueue(object: Callback<FollowListRes> {

            override fun onResponse(
                call: Call<FollowListRes>,
                response: Response<FollowListRes>
            ) {
                Log.d("FOLLOWER/SUCCESS",response.toString())
                val resp: FollowListRes = response.body()!!
                Log.d("FOLLOWER/SUCCESS", resp.toString())
                when(val code = resp.code){
                    1000-> getFollowerView.onFollowerSuccess(resp.result)
                    else-> getFollowerView.onFollowerFailure(code, resp.message)

                }
            }

            override fun onFailure(call: Call<FollowListRes>, t: Throwable) {
                Log.d("FOLLOWER/FAILURE", t.message.toString())
            }
        })

    }

    fun getFollowingList(token: String) {
        val followingService = getRetrofit().create(FollowRetrofitInterface::class.java)
        followingService.getFollowingList(token).enqueue(object: Callback<FollowListRes> {

            override fun onResponse(
                call: Call<FollowListRes>,
                response: Response<FollowListRes>
            ) {
                Log.d("FOLLOWING/SUCCESS", response.toString())
                val resp: FollowListRes = response.body()!!
                Log.d("FOLLOWING/SUCCESS", resp.toString())
                when(val code = resp.code){
                    1000-> getFollowingView.onFollowingSuccess(resp.result)
                    else-> getFollowingView.onFollowingFailure(code, resp.message)
                }
            }

            override fun onFailure(call: Call<FollowListRes>, t: Throwable) {
                Log.d("FOLLOWING/FAILURE", t.message.toString())
            }
        })
    }

}