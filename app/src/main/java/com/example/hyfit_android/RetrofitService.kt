package com.example.hyfit_android

import android.util.Log
import com.example.hyfit_android.Join.JoinReq
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RetrofitService {

//    fun login(loginRequest: LoginReq){
//        val userService = getRetrofit().create(RetrofitInterface::class.java)
//        Log.d("email: ", loginRequest.email)
//        Log.d("password: ", loginRequest.password)
//        userService.login(loginRequest).enqueue(object : Callback<UserResponse>{
//            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
//                val resp: UserResponse = response.body()!!
//                when(val code = resp.code){
//                    1000 -> Log.d("Success","${resp.result}")
//                    else -> Log.d("error", "$code")
//                }
//            }
//
//            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
//                Log.d("Failure", t.message.toString())
//            }
//        })
//    }

    fun join(joinRequest : JoinReq) {

        val userService = getRetrofit().create(RetrofitInterface::class.java)
        Log.d("email: ", joinRequest.email)
        Log.d("password: ", joinRequest.password)
        userService.join(joinRequest).enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                val resp: UserResponse = response.body()!!
                when (val code = resp.code) {
                    1000 -> Log.d("Success", "$code")
                    else -> Log.d("error", "$code")
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                Log.d("Failure", t.message.toString())
            }
        })
    }
}
