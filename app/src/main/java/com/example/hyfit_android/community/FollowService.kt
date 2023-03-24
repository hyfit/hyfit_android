package com.example.hyfit_android.community

import android.util.Log
import com.example.hyfit_android.getRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowService {
    private lateinit var getFollowerView: GetFollowerView
    private lateinit var getFollowingView: GetFollowingView

    fun setFollowerView(getFollowerView: GetFollowerView){
        this.getFollowerView = getFollowerView
    }
    fun setFollowingView(getFollowingView: GetFollowingView){
        this.getFollowingView = getFollowingView
    }

    fun getFollowerList(token:String) {
        val followService = getRetrofit().create(FollowRetrofitInterface::class.java)
        followService.getFollowerList(token).enqueue(object: Callback<FollowResponse> {

            override fun onResponse(
                call: Call<FollowResponse>,
                response: Response<FollowResponse>
            ) {
                Log.d("FOLLOWER/SUCCESS",response.toString())
                val resp: FollowResponse = response.body()!!
                Log.d("FOLLOWER/SUCCESS", resp.toString())
                when(val code = resp.code){
                    1000-> getFollowerView.onFollowerSuccess(code, resp.result)
                    else-> getFollowerView.onFollowerFailure(code, resp.message)

                }
            }

            override fun onFailure(call: Call<FollowResponse>, t: Throwable) {
                Log.d("FOLLOWER/FAILURE", t.message.toString())
            }
        })

    }

    fun getFollowingList(token: String) {
        val followingService = getRetrofit().create(FollowRetrofitInterface::class.java)
        followingService.getFollowingList(token).enqueue(object: Callback<FollowResponse> {

            override fun onResponse(
                call: Call<FollowResponse>,
                response: Response<FollowResponse>
            ) {
                Log.d("FOLLOWING/SUCCESS", response.toString())
                val resp: FollowResponse = response.body()!!
                Log.d("FOLLOWING/SUCCESS", resp.toString())
                when(val code = resp.code){
                    1000-> getFollowingView.onFollowingSuccess(code, resp.result)
                    else-> getFollowingView.onFollowingFailure(code, resp.message)
                }
            }

            override fun onFailure(call: Call<FollowResponse>, t: Throwable) {
                Log.d("FOLLOWING/FAILURE", t.message.toString())
            }
        })
    }

}